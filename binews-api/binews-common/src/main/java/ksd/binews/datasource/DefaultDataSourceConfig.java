package ksd.binews.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

@Configuration
@MapperScan(basePackages = DefaultDataSourceConfig.PACKAGES, sqlSessionTemplateRef  = DefaultDataSourceConfig.TEMPLATE)
public class DefaultDataSourceConfig {

	static final String PACKAGES = "ksd.binews.mapper";
	static final String TEMPLATE = "binewsSqlSessionTemplate";
	static final String BEAN_NAME = "binewsDataSource";
	static final String PREFIX = "spring.datasource.binews";
	
	@Bean(name = BEAN_NAME)
    @ConfigurationProperties(prefix = PREFIX)
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "binewsSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier(BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        //配置pagehelper分页插件拦截
        bean.setPlugins(new Interceptor[]{pageHelper()});
        return bean.getObject();
    }

    @Bean(name = "binewsTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier(BEAN_NAME) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = TEMPLATE)
    @Primary 
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("binewsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    /**
	 * 配置mybatis的分页插件pageHelper
	 * @return
	 */
	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "mysql"); // 配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}
	
}
