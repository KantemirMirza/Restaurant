package com.kani.restaurant.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.entity.Bill;
import com.kani.restaurant.jwt.JwtAuthenticationFilter;
import com.kani.restaurant.repository.IBillRepository;
import com.kani.restaurant.service.IBillService;
import com.kani.restaurant.util.CafeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillService implements IBillService {
    private final IBillRepository billRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    public ResponseEntity<String> generateBill(Map<String, String> requestMap) {
        log.info("Inside generateBill");
        try{
            String fileName;
            if(validateRequestMap(requestMap)){

                if(requestMap.containsKey("isGenerate") && Boolean.FALSE.equals(requestMap.get("isGenerate"))){
                    fileName = (String) requestMap.get("uuid");
                }else{
                    fileName = CafeUtil.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }

                String data = "Name:" + requestMap.get("billName") + "\n" +
                        "Contact Number:" + requestMap.get("contactNumber") +
                        "\n" + "E-Mail:" + requestMap.get("email") + "\n" + "Payment Method:" + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeResponse.STORE_LOCATION + "\\" + fileName + ".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph paragraph = new Paragraph("Cafe Management System", getFont("Header"));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
                Paragraph paragraph1 = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph1);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray json = CafeUtil.getJsonArray((String) requestMap.get("productDetails"));
                for (int i = 0; i < json.length(); i++) {
                    addRows(table, CafeUtil.getMapFromJson(json.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total: " + requestMap.get("totalAmount")+
                        "\n" + "Thank You For Visiting. See You Again !!", getFont("Data") );
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
            }
            return CafeUtil.getResponseEntity("Bill not found", HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if(jwtAuthenticationFilter.isAdmin()){
            list = billRepository.getAllBills();
        }else{
            list = billRepository.getBillByUserName(jwtAuthenticationFilter.currentUsername());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, String> requestMap) {
        log.info("Inside getPdf : requestMap {}", requestMap);
        try {
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uuid") || validateRequestMap(requestMap)) {
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            }

            String filePath = CafeResponse.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
            if (CafeUtil.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                requestMap.put("isGenerate", String.valueOf(false));
                generateBill(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteBill(Long id) {
        try{
            Optional<Bill> optional = billRepository.findById(id);
            if(optional.isPresent()){
                billRepository.deleteById(id);
                return CafeUtil.getResponseEntity("Bill deleted successfully", HttpStatus.OK);
            }
            return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.OK);
        }catch (Exception ex ){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws Exception{
        if (filePath == null) {
            throw new IllegalArgumentException("Not must be path null");
        }

        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("Not Found: " + filePath);
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new IOException("Unknown Error: " + filePath, e);
        }
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("billName"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell((String) data.get("totalAmount"));
        table.addCell((String) data.get("totalAmount"));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle-> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type){
        log.info("Inside getFont");
        switch (type){
        case "Header":
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
            headerFont.setStyle(Font.BOLD);
            return headerFont;
            case "data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
            dataFont.setStyle(Font.BOLD);
            return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document)throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rect = new Rectangle(577,825,18,15);
        rect.disableBorderSide(1);
        rect.disableBorderSide(2);
        rect.disableBorderSide(4);
        rect.disableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void insertBill(Map<String, String> requestMap) {
        Bill bill = new Bill();
        bill.setUuid(requestMap.get("uuid"));
        bill.setBillName(requestMap.get("billName"));
        bill.setEmail(requestMap.get("email"));
        bill.setContactNumber(requestMap.get("contactNumber"));
        bill.setPaymentMethod(requestMap.get("paymentMethod"));
        bill.setTotalAmount(Double.parseDouble((String)requestMap.get("totalAmount")));
        bill.setProductDetail(requestMap.get("productDetail"));
        bill.setCreatedBy(jwtAuthenticationFilter.currentUsername());
        billRepository.save(bill);
    }

    private boolean validateRequestMap(Map<String, String> requestMap) {
        return requestMap.containsKey("billName")&&
                requestMap.containsKey("contactNumber")&&
                requestMap.containsKey("email")&&
                requestMap.containsKey("paymentMethod")&&
                requestMap.containsKey("productDetails")&&
                requestMap.containsKey("totalAmount");
    }
}
