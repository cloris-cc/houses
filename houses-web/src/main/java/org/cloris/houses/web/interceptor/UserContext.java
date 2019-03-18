package org.cloris.houses.web.interceptor;

import org.cloris.houses.common.model.User;

/**
 * 保存 Session 中 User 对象的 Context。
 * 用 ThreadLocal 实现是因为每一个请求都在一个独立的线程中，保证多线程并发的数据一致性。
 *
 * @author Jackson Fang
 * Date:   2018/11/8
 * Time:   15:47
 */
public class UserContext {
    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

    public static User getUser() {
        return USER_THREAD_LOCAL.get();
    }

}
