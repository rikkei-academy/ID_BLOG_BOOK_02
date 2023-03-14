package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.CartDetail;
import ra.model.entity.Carts;
import ra.model.repository.CartDetailRepository;
import ra.model.service.CartDetailService;


import java.util.List;


@Service
public class CartDetailServiceImp implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;

    public Page<CartDetail> getAllList(Pageable pageable) {
        return cartDetailRepository.findAll(pageable);
    }

    @Override
    public CartDetail saveOrUpdate(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    @Override
    public CartDetail findById(Integer id) {
        return cartDetailRepository.findById(id).get();
    }

    @Override
    public Page<CartDetail> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public List<CartDetail> findAll() {
        return cartDetailRepository.findAll();
    }


    @Override
    public CartDetail findByProduct_ProductIdAndCarts_CartId(Integer bookId, Integer cartId) {
        return cartDetailRepository.findByProduct_ProductIdAndCarts_CartId(bookId ,cartId);
    }

    @Override
    public boolean deleteByCartDetailId(Integer id) {
        try {
            cartDetailRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<CartDetail> findByProduct_ProductId(int bookId) {
        return cartDetailRepository.findByProduct_ProductId(bookId);
    }

    @Override
    public List<CartDetail> findByCartsIn(List<Carts> listCart) {
        return cartDetailRepository.findByCartsIn(listCart);
    }


}