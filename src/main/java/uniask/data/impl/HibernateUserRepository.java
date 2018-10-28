package uniask.data.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import uniask.data.api.UserRepository;
import uniask.model.User;

@Repository
public class HibernateUserRepository implements UserRepository {
    private SessionFactory sessionFactory;
    private boolean indexed = false;

    @Autowired
    public HibernateUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    private FullTextSession currentFullTextSession() {
        FullTextSession fullTextSession = Search.getFullTextSession(currentSession());
        //TODO：这一步最好在Spring初始化完成之后做
        if (indexed == false) {
            try {
                fullTextSession.createIndexer().startAndWait();
                indexed = true;
            } catch (InterruptedException e) {
                /*
                TODO:
                    索引创建失败
                    当前查询返回空，需要在下一次查询时再创建一次索引
                    或许此时应该直接去查数据库
                */
            }
        }
        return fullTextSession;
    }

    @Override
    public long save(User user) {
        Serializable id = currentSession().save(user);
        return (long) id;
    }

    @Override
    public User findByEmail(String email) {
        String hql = "from User where email =:email";
        Query query = currentSession().createQuery(hql);
        query.setParameter("email", email);
        if (query.list().size() == 0) {
            return null;
        }
        return (User) query.list().get(0);
    }

    @Override
    public User findById(long id) {
        String hql = "from User where id =:id";
        Query query = currentSession().createQuery(hql);
        query.setParameter("id", id);
        if (query.list().size() == 0) {
            return null;
        }
        return (User) query.list().get(0);
    }
}
