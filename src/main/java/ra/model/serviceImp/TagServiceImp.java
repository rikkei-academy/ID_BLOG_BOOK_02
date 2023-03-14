package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Tag;
import ra.model.repository.TagRepository;
import ra.model.service.TagService;
import ra.payload.request.TagRequest;

import java.util.List;
@Service
public class TagServiceImp implements TagService {
    @Autowired private TagRepository tagRepository;
    @Override
    public Page<Tag> getAllList(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag saveOrUpdate(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag findById(Integer id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public Page<Tag> findByName(String name, Pageable pageable) {
        return tagRepository.findByTagNameContaining(name, pageable);
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag mapTagRequestToTag(TagRequest request) {
        Tag tag= new Tag();
        tag.setTagName(request.getTagName());
        tag.setTagStatus(true);
        return tag;
    }
}