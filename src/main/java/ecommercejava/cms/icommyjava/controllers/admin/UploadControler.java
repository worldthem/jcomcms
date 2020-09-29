package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Gallery;
import ecommercejava.cms.icommyjava.repository.GalleryRepository;
import ecommercejava.cms.icommyjava.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UploadControler {
/*
    private final StorageService storageService;
    @Autowired
    public UploadControler(StorageService storageService) {
        this.storageService = storageService;
    }
   */

    @Autowired
    GalleryRepository galleryRepository;

    private void saveGallery(Integer id, String Directory, String title, String type ){
       if(id!=0) {
           Gallery gallery = new Gallery();
           gallery.setTitle(title);
           gallery.setDirectory(Directory);
           gallery.setTypefile(type);
           gallery.setProductid(id);
           galleryRepository.save(gallery);
       }
    }


    @PostMapping("/admin/upload-simple/simple")
    public @ResponseBody
    String simple_image(@RequestParam("file0") MultipartFile file, HttpServletRequest request, @RequestParam(value ="type", required = false) String type) {
         StorageService storageService = new StorageService();
         String urlimg= "/content/images/"+storageService.uploadSingleImg(file,  "content/images");
         return type!=null ? "<img src=\""+urlimg+"\" class=\"bg_image\">": urlimg;
    }


    @PostMapping("/admin/upload/upload-file")
    public @ResponseBody
    String handleFileUpload(@RequestParam("file0") MultipartFile file, HttpServletRequest request) {
         String galID = request.getParameter("parent_id") != null ? request.getParameter("parent_id") : "0";
                galID = galID.replace(",","").replace(".","");

          int id = Integer.parseInt(galID);
         String title =  request.getParameter("field1");

         StorageService storageService = new StorageService();
         String Directory = storageService.simpleUpload(file,  "content/files");

         saveGallery(id, Directory, title,"file" );

         List<Gallery> galleries = galleryRepository.findByProductid(id);
        try {
            return Helpers.adminGallery(galleries, "file");
        }catch (Exception e){
            return "";
        }

    }



    @PostMapping("/admin/upload/upload-images")

    public @ResponseBody
    String handleImagesUpload(@RequestParam(value ="file0", required=false) MultipartFile file, HttpServletRequest request
            , @RequestParam(value ="file1", required=false) MultipartFile file1  , @RequestParam(value ="file2", required=false) MultipartFile file2
            , @RequestParam(value ="file3", required=false) MultipartFile file3, @RequestParam(value ="file4", required=false) MultipartFile file4
            , @RequestParam(value ="file5", required=false) MultipartFile file5 ) {

        List<MultipartFile> arrayfile = new ArrayList<MultipartFile>();
        if(file !=null)
            arrayfile.add(file);
        if(file1 !=null)
            arrayfile.add(file1);
        if(file2 !=null)
            arrayfile.add(file2);
        if(file3 !=null)
            arrayfile.add(file3);
        if(file4 !=null)
            arrayfile.add(file4);
        if(file5 !=null)
            arrayfile.add(file5);

            StorageService storageService = new StorageService();
            List<String> list = storageService.uploadMultipleImages(arrayfile, "content/imgproduct");

            String galID = request.getParameter("parent_id") != null ? request.getParameter("parent_id") : "0";
                   galID = galID.replace(",","").replace(".","");

             int id = Integer.parseInt(galID);

            if (list != null && list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                       saveGallery(id, list.get(i), "","gallery" );
                 }
            }


              List<Gallery> galleries = galleryRepository.findByProductid(id);
              try {
                  return Helpers.adminGallery(galleries, "gallery");
              }catch (Exception e){
                  return "";
              }

  }

    @PostMapping("admin/gallery/remove-image/{id}/{prid}/{type}")

    public @ResponseBody
    String remove_image(@PathVariable String id, @PathVariable String prid, @PathVariable String type) {

          String imgName="";
        try{
            Gallery gallery = galleryRepository.getOne(Integer.parseInt(id));
            imgName = gallery.getDirectory();

            galleryRepository.deleteById(Integer.parseInt(id));
          }catch (Exception e){
            System.out.println("errrorrGallery: " + e);
          }

        try{
            if(!imgName.isEmpty() && type.contains("gallery")) {
                File file11 = new File(Paths.get("content/imgproduct", imgName).toString());
                file11.delete(); // delete image
                File file1122 = new File(Paths.get("content/imgproduct", "thumb_" + imgName).toString());
                file1122.delete(); // delete thumb
            }
            if(!type.contains("file")){
                File file11 = new File(Paths.get("content/files", imgName).toString());
                file11.delete();
            }
        }catch (Exception d){System.out.println("err delete: " + d);}


        List<Gallery> galleries = galleryRepository.findByProductid(Integer.parseInt(prid));
        try {
            return Helpers.adminGallery(galleries, type);
        }catch (Exception e){
            System.out.println("errors"+e);
            return "";
        }
    }




}
