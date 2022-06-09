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
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    DummyJpaController dummyController = new DummyJpaController(); //memanggil jpa controller
    List<Dummy> data = new ArrayList<>(); //data disimpan dalam bentul array list
    
    @RequestMapping("/read") //untuk menampilkan daftar dummy yang sudah di input
    public String getDummy(Model model) {
    try {
      data = dummyController.findDummyEntities(); //untuk menemukan data dummy yang sudah diinput

    } catch (Exception e) { 

    }
    model.addAttribute("data", data); //untuk menampilkan model data yang sudah diinput
    return "dummy"; //kembali ke dummynya
  }

  @RequestMapping("/create") //req mapping untuk menginput data
  public String createDummy() {
    return "dummy/create";
  }

  @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //mapping ini untuk mempost data yang ditambahkan dalam bentuk multipart data
  @ResponseBody
  public String newDummyData(@RequestParam("gambar") MultipartFile file, HttpServletRequest data, HttpServletResponse res) throws ParseException, Exception {
    Dummy dummy = new Dummy();
    int id = Integer.parseInt(data.getParameter("id")); //fungsi parse ini untuk convert format data
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.getParameter("tanggal"));
    byte[] img = file.getBytes();
    //set untuk mengatur format data yang diinput
    dummy.setId(id);
    dummy.setTanggal(date);
    dummy.setGambar(img);

    dummyController.create(dummy);
    res.sendRedirect("/read"); //direct ke page read
    return "created";
  }
    
  //untuk mengambil data gambar yang telah diinput
    @RequestMapping(value = "/img", method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    //untuk merespon get data
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
    Dummy dummy = dummyController.findDummy(id);
    byte[] img = dummy.getGambar();
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
  }

  @RequestMapping("/edit/{id}") //direct ke edit page
  public String updateDummy(@PathVariable("id") int id, Model model) throws Exception { //data dieedit berdasarkan primary keynya yaitu id
    Dummy dummy = dummyController.findDummy(id);
    model.addAttribute("data", dummy); //untuk menampilkan model data yang sudah diinput
    return "dummy/edit";
  }

  @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //post data yang diedit
  @ResponseBody
  public String updateDummyData(@RequestParam("gambar") MultipartFile file, HttpServletRequest data, HttpServletResponse res) throws ParseException, Exception {
    Dummy dummy = new Dummy();

    int id = Integer.parseInt(data.getParameter("id")); //fungsi parse ini untuk convert format data
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.getParameter("tanggal"));
    byte[] img = file.getBytes();
    //set untuk mengatur format data yang diinput
    dummy.setId(id);
    dummy.setTanggal(date);
    dummy.setGambar(img);

    dummyController.edit(dummy);
    res.sendRedirect("/read"); //direct ke page read
    return "updated";
  }
  
  @GetMapping("/delete/{id}") //untuk menghapus data dummy
  @ResponseBody
  public String deleteDummy(@PathVariable("id") int id, HttpServletResponse res) throws Exception {
    dummyController.destroy(id); //menghapusnya berdasarkan primary key
    res.sendRedirect("/read");
    return "deleted";
  }
}