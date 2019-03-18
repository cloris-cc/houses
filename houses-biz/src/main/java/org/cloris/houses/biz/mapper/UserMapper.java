package org.cloris.houses.biz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cloris.houses.common.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findAllUsers();

    /**
     * 增加一个用户。
     *
     * @param account
     * @return 影响的行数
     */
    int insert(User account);

    /**
     * 删除邮箱。
     *
     * @param email
     * @return 影响的行数
     */
    int delete(String email);

    int update(User updateUser);

    User findUser(@Param("email") String email, @Param("dbPass") String dbPass);
}
