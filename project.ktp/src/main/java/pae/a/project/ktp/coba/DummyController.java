/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pae.a.project.ktp.coba;

import antlr.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static java.util.Date.parse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    @ResponseBody
    public List<Dummy> getDummy(){
        try{
            data = dummyController.findDummyEntities();
        }
        catch (Exception e){}
        return data;
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
    @PostMapping(value="/newdata", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String newDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest data) throws ParseException, Exception{
        Dummy dummy = new Dummy();
        
        int id = Integer.parseInt(data.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.getParameter("tanggal"));
        //String filename = StringUtils.CleanPath(file.getOriginalFilename());
        byte[] img = file.getBytes();
        dummy.setId(id);
        dummy.setTanggal(date);
        dummy.setGambar(img);
        
        dummyController.create(dummy);
        return "created";
    }
    
    @RequestMapping(value = "/img", method = RequestMethod.GET ,produces = {
    MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
  })
  public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
    Dummy dummy = dummyController.findDummy(id);
    byte[] img = dummy.getGambar();
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
  }
}
