package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.services.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Controller
public class Themes extends MainController {
    @Autowired
    StorageService storageService;

    @RequestMapping(value ="/admin/themes", method = RequestMethod.GET)
    public ModelAndView show(Model model, HttpServletRequest request) {
        File[] directories = new File(HelpersFile.pathToFile("content/views/themes/")).listFiles(File::isDirectory);
        model.addAttribute("title", "Themes");
        model.addAttribute("rows", directories);
        model.addAttribute("activeTheme", Helpers.getConfig().getTheme());
        return view("admin::pages/themes") ;
    }


    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/themes/activate") // Map ONLY POST Requests

    public  String activateTheme (HttpServletRequest request, RedirectAttributes redirectAttributes ) {
        try {
             Config config = Helpers.getConfig();
             config.setTheme(request.getParameter("theme"));
             config.save();

        }catch (Exception e){
            System.out.println(e);
        }

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }

    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/themes/delete") // Map ONLY POST Requests

    public  String delete (HttpServletRequest request, RedirectAttributes redirectAttributes ) {
        try {
             FileUtils.deleteDirectory(new File(HelpersFile.pathToFile("content/views/themes/"+request.getParameter("theme"))));
        }catch (Exception e){
             System.out.println(e);
        }

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }

    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/themes/store") // Map ONLY POST Requests

    public  String store (HttpServletRequest request, RedirectAttributes redirectAttributes,
                          @RequestParam(value ="theme", required=false, defaultValue = "null") MultipartFile theme) {



        try {
             String filePath = storageService.uploadToLocalFileSystem(theme, "content/views/themes/");
             //unzipTheme("content/views/themes/"+filePath);
            String fileName= filePath.replace(".zip","");
            Path source = Paths.get(HelpersFile.pathToFile("content/views/themes/"+filePath));
            Path target = Paths.get(HelpersFile.pathToFile("content/views/themes/"+fileName+"/"));

             unzipFolder(source, target);
             Config config = Helpers.getConfig();
            config.setTheme(fileName);
            config.save();

           File file=new File(HelpersFile.pathToFile("content/views/themes/"+filePath));
                     file.delete();

        }catch (Exception e){
            System.out.println(e);
        }

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }



     private void unzipFolder(Path source, Path target) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }


    // protect zip slip attack
    private Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }



}
