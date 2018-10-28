package uniask.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan("uniask")
public class RepositoryConfig implements TransactionManagementConfigurer {

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory());
        return transactionManager;
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder edb = new EmbeddedDatabaseBuilder();
        edb.setType(EmbeddedDatabaseType.H2);
        edb.setScriptEncoding("UTF-8");
        edb.addScript("sql/schema.sql");
        return edb.build();
    }

    @Bean
    public SessionFactory sessionFactory() {
        try {
            LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
            lsfb.setDataSource(dataSource());
            lsfb.setPackagesToScan("uniask.model");
            Properties props = new Properties();
            props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
            props.put("hibernate.show_sql", true);
            // 自动建表，如果表存在则不再建表
            props.put("hibernate.hbm2ddl.auto", "update");
            // 必须将上面的配置由update改为create才可以import files
            // props.put("hibernate.hbm2ddl.import_files","src/main/resources/sql/add-user.sql");
            // props.put("hibernate.hbm2ddl.import_files","src/main/resources/sql/new-test-data.sql");
            // integrate hibernate search
            props.setProperty("hibernate.search.default.directory_provider", "filesystem");
            props.setProperty("hibernate.search.default.indexBase", "target/lucene/indexes");
            props.setProperty("hibernate.search.lucene_version", "LUCENE_CURRENT");
            lsfb.setHibernateProperties(props);
            lsfb.afterPropertiesSet();
            return lsfb.getObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
