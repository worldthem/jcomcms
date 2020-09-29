package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.controllers.MainController;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public class MainControllerAdmin extends MainController {
  /*
    private final StorageService storageService;

    @Autowired
    public MainControllerAdmin(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

 */

    public String uploadFile(Model model, MultipartFile file){
        return "";
    }

}
