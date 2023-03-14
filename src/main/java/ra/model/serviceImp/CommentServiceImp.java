//package ra.model.serviceImp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import ra.model.entity.Comment;
//import ra.model.repository.CommentRepository;
//import ra.model.service.CommentService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CommentServiceImp implements CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//    @Override
//    public List<Comment> findChildById(int comId) {
//        return commentRepository.findChildById(comId);
//    }
//
//    @Override
//    public List<Comment> findParentById(int comId) {
//        return commentRepository.findParentById(comId);
//    }
//
//    @Override
//    public List<Comment> findByCommentParentId(int comId) {
//        return commentRepository.findByCommentParentId(comId);
//    }
//
//    @Override
//    public List<Comment> findByCommentIdIn(ArrayList<Integer> listComId) {
//        return commentRepository.findByCommentIdIn(listComId);
//    }
//
//    @Override
//    public Page<Comment> findByProduct_ProductName(String bookName, Pageable pageable) {
//        return commentRepository.findByProduct_ProductName(bookName,pageable);
//    }
//
//    @Override
//    public Page<Comment> findByUsers_UserName(String usersName, Pageable pageable) {
//        return commentRepository.findByUsers_UserName(usersName,pageable);
//    }
//
//    @Override
//    public Page<Comment> getAllList(Pageable pageable) {
//        return commentRepository.findAll(pageable);
//    }
//
//    @Override
//    public Comment saveOrUpdate(Comment comment) {
//        return commentRepository.save(comment);
//    }
//
//    @Override
//    public Comment findById(Integer id) {
//        return commentRepository.findById(id).get();
//    }
//
//    @Override
//    public Page<Comment> findByName(String name, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public List<Comment> findAll() {
//        return null;
//    }
//}
