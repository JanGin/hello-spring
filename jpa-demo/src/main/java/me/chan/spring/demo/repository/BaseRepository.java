package me.chan.spring.demo.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by JanGin.
 */
@NoRepositoryBean
public interface BaseRepository<T, Long> extends PagingAndSortingRepository<T, Long> {

    //按照更新时间倒序Id正序查找最近5条记录
    List<T> findTop5ByOrderByUpdateTimeDescIdAsc();
}
