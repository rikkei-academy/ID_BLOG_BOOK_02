package ra.model.service;

import org.springframework.stereotype.Repository;

import ra.model.entity.Tag;
import ra.payload.request.TagRequest;

@Repository
public interface TagService extends RootService<Tag,Integer>{
    Tag mapTagRequestToTag(TagRequest request);
}