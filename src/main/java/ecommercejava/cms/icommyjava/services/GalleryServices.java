package ecommercejava.cms.icommyjava.services;



import ecommercejava.cms.icommyjava.entity.Gallery;
import ecommercejava.cms.icommyjava.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryServices {
    @Autowired
    GalleryRepository galleryRepository;

    public void updateGalleryByProductId(Integer prid, Integer temporarid){
        List<Gallery> galleries = galleryRepository.findByProductid(temporarid);
        for(Gallery gallery: galleries){
            gallery.setProductid(prid);
            galleryRepository.save(gallery);
        }
    }

    public Gallery save(Gallery gallery){
        return galleryRepository.save(gallery);
    }

    public void delete(Integer id){
        galleryRepository.deleteById(id);
    }

}
