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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

@Configuration
@MapperScan(basePackages = BinewsPriceDataSourceConfig.PACKAGES, sqlSessionTemplateRef  = BinewsPriceDataSourceConfig.TEMPLATE)
public class BinewsPriceDataSourceConfig {

	static final String PACKAGES = "ksd.binews.mapper.price";
	static final String TEMPLATE = "binewsPriceSqlSessionTemplate";
	static final String BEAN_NAME = "binewsPriceDataSource";
	static final String PREFIX = "spring.datasource.binews-price";
	
	@Bean(name = BEAN_NAME)
    @ConfigurationProperties(prefix = PREFIX)
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "binewsPriceSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier(BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/price/*.xml"));
        //配置pagehelper分页插件拦截
        bean.setPlugins(new Interceptor[]{pageHelper()});
        return bean.getObject();
    }

    @Bean(name = "binewsPriceTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier(BEAN_NAME) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = TEMPLATE)
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("binewsPriceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
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
