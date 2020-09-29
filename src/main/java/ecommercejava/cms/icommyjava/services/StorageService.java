package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class StorageService {


public String uploadSingleImg(MultipartFile file, String Path){
    try {
        Path = Path.isEmpty()? "content/imgproduct" : Path;
        //Path = HelpersFile.pathToFile(Path);
        List<MultipartFile> arrayfile = new ArrayList<MultipartFile>();
        arrayfile.add(file);
        List<String> list = uploadMultipleImages(arrayfile, Path);
        return list.get(0) ;

    }catch (Exception e){
        return "";
    }

}

public List<String> uploadMultipleImages(List<MultipartFile> files, String Path){
    Path = Path.isEmpty()? "content/imgproduct" : Path;
    Path = HelpersFile.pathToFile(Path);
   List<String> returnImg = new ArrayList<String>();
    for(int i=0;i<files.size();i++) {
        try {
            // Get the filename and build the local file path (be sure that the
            // application have write permissions on such directory)
            String[] fname = files.get(i).getOriginalFilename().split(Pattern.quote("."));
            String filename = Helpers.randomString(25)+"."+fname[fname.length-1]; //files.get(i).getOriginalFilename();
            String filename_thumb = "thumb_"+filename; //files.get(i).getOriginalFilename();
            String filename_thumb_small = "thumb_small_"+filename; //files.get(i).getOriginalFilename();
            String directory = Path;
            String filepath = Paths.get(directory, filename).toString();
            String filepathThumb = Paths.get(directory, filename_thumb).toString();
            String filepathThumbSmall = Paths.get(directory, filename_thumb_small ).toString();
            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(files.get(i).getBytes());
            stream.close();

            boolean crop =  Helpers.getConfig().getMinOptions().getMedia_crop() !=null ? true: false;
            String heightConfig = Helpers.getConfig().getMinOptions().getMedia_height();
            String widthConfig = Helpers.getConfig().getMinOptions().getMedia_width();

            int max_width  = widthConfig==null? 400 : Integer.parseInt(widthConfig);
            int max_height = heightConfig==null? 400 : Integer.parseInt(heightConfig);

            // Crop the image (uploadfile is an object of type MultipartFile)
            BufferedImage croppedImage = resizeImage(files.get(i).getBytes(), max_width, max_height, crop);
           // Get the file extension
            String ext = FilenameUtils.getExtension(filename_thumb);
           // Save the file locally
            File outputfile = new File(filepathThumb);
            ImageIO.write(croppedImage, ext, outputfile);

             // generate a very small image
            max_height = (40*max_height)/max_width;
            max_width = 40;
            File outputfileSmall = new File(filepathThumbSmall);

            BufferedImage croppedImage2 = resizeImage(files.get(i).getBytes(), max_width, max_height, crop);
            ImageIO.write(croppedImage2, ext, outputfileSmall);



            returnImg.add(filename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
   return returnImg;
}



    private BufferedImage resizeImage(byte[] image, int max_width, int max_height, boolean crop){


        try {
            InputStream in = new ByteArrayInputStream(image);
            BufferedImage originalImage = ImageIO.read(in);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            int width_new =  (max_height *  width )/ height;
            int height_new =  (max_width *  height) / width;
            int x = 0;
            int y = 0;

           if(crop) {
               if (height_new < max_height) {
                   height_new = max_height;
                   x= (max_width-width_new)/2;
               } else {
                   width_new = max_width;
                   y=(max_height-height_new)/2;
               }
           }else{
               max_height = height_new;
               width_new = max_width;
            }


            BufferedImage resizedImage = new BufferedImage(max_width,  max_height,  BufferedImage.TYPE_INT_RGB);//TYPE_INT_RGB ,  TYPE_3BYTE_BGR
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, x, y, width_new,  height_new, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            return resizedImage;
        }catch (Exception e){
            return  null;
         }
     }



    private BufferedImage cropImageSquare(byte[] image, int max_width, int max_height, boolean crop) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        int width_new =  (max_height *  width )/ height;
        int height_new =  (max_width *  height) / width;
        int x = 0;
        int y = 0;

        if(crop) {
            if (height_new < max_height) {
                height_new = max_height;
                x= (max_width-width_new)/2;
            } else {
                width_new = max_width;
                y=(max_height-height_new)/2;
            }
        }else{
            max_height = height_new;
            width_new = max_width;
        }



        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                x, // x coordinate of the upper-left corner
                y, // y coordinate of the upper-left corner
                width_new,            // widht
                height_new            // height
        );

        return croppedImage;
    }

    public String simpleUpload(MultipartFile file, String directory){
        directory = HelpersFile.pathToFile(directory);
        String filename ="";
        try {

            // Get the filename and build the local file path (be sure that the
            // application have write permissions on such directory)
            String filenameOriginal = file.getOriginalFilename();
            String[] fname = filenameOriginal.split(Pattern.quote("."));
                     filename = Helpers.randomString(25)+"."+fname[fname.length-1];
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(file.getBytes());
            stream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
      return filename;
    }

    public String uploadToLocalFileSystem(MultipartFile file, String directory) {
        directory = HelpersFile.pathToFile(directory);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(directory + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(directory)
                .path(fileName)
                .toUriString();
        return fileName;
    }

}
