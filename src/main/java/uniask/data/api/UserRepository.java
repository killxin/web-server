package uniask.data.api;

import java.util.List;
import uniask.model.User;

/**
 * Created by young on 2017/2/28.
 */
public interface UserRepository {
    // 保存用户
    long save(User user);

    // 查找用户
    User findByEmail(String email);

    User findById(long id);

}
