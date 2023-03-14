

package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Tag;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Page<Tag> findByTagNameContaining(String name, Pageable pageable);
}