package board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties") //application.properties를 설정파일로 사용할 수 있도록 지정.
public class DatabaseConfiguration {
	
//스프링 컨텍스트를 주입받기 위한 필드 선언
	@Autowired
    private ApplicationContext applicationContext;
	
	
//히카리CP 설정 - JDBC DataSource  구현체 (JDBC의 커넥션 풀 오픈소스 라이브러리)
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari") //application.properties에서 설정했던 DB관련 정보를 사용하도록 지정. spring.datasource.hikari로 시작하는 설정을 이용해 히카리CP의 설정파일을 만듦
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

//DataSource 빈 생성
	@Bean
	public DataSource dataSource() throws Exception {
		//히카리CP DataSource
		DataSource dataSource = new HikariDataSource(hikariConfig());
		System.out.println(dataSource.toString()); //데이터 소스 설정됐는지 확인용.
		return dataSource;
	}

	
//SqlSessionFactory - MyBatis 프레임워크에서 SQL 세션을 생성하는 역할, SQL세션은 DB와 상호작용하고 SQl문 실행, 결과반환
//SqlSessionFactoryBean은 MyBatis의 구성을 설정하고 마이바티스의 SqlSessionFactory생성에 사용됨.
//dataSource : DB연결에 쓸 데이터 소스 설정
//mapperLocation : SQL 매퍼 파일 위치 설정
//SQL 매퍼 : SQL문을 정의한 XML파일

//SqlSessionFactory 생성 --> MyBatis 구성 설정 + DataSource ==> SQL세션 생성    | 이후에 SqlSessionTemplate을 통해 SQL 세션을 사용, DB와 상호작용 가능.
	@Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/sql-*.xml")); //이름이 sql로 시작하고, 확장자가 xml인 모든 파일

        sqlSessionFactoryBean.setConfiguration(mybatisConfig());
        
        return sqlSessionFactoryBean.getObject();
    }

//SqlSessionTemplate
//MyBatis SQL세션을 사용하기위한 편리한 기능 제공
/*
1. SQL 실행 : select, insert, update, delete 등 + 필요에 따라 파라미터 전달, 결과 반환
2. Transaction 관리 : 여러개의 SQL문을 단일 트랜잭션으로 묶을 수 있음.
3. 예외처리 : MyBatis가 발생시키는 예외를 스프링의 예외 계층 구조에 맞게 변환. -> 스프링의 예외처리 기능을 활용하여 예외처리 수행 가능.
 */
	@Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
	
	
	
	@Bean
	@ConfigurationProperties(prefix="mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfig() {
		return new org.apache.ibatis.session.Configuration();
	}
}
