package com.lingDream.root.service;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.toolkit.*;
import com.lingDream.root.mapper.MyMapper;
import com.lingDream.root.tool.MyPage;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;


public abstract class MyService<T>
        implements  IService<T>{
    //region 主要属性
    protected MyMapper<T> baseMapper;

    public MyService(MyMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }
    //endregion

    //region 复制的

    // region ??
    protected static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    protected Class<T> currentModelClass() {
        return ReflectionKit.getSuperClassGenricType(this.getClass(), 1);
    }

    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(this.currentModelClass());
    }

    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(this.currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }
    //endregion

    //region 新增
    @Transactional(rollbackFor = {Exception.class})
    public boolean insert(T entity) {
        return retBool(this.baseMapper.insert(entity));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertAllColumn(T entity) {
        return retBool(this.baseMapper.insertAllColumn(entity));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertBatch(List<T> entityList) {
        return this.insertBatch(entityList, 30);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        } else {
            try {
                SqlSession batchSqlSession = this.sqlSessionBatch();
                Throwable var4 = null;

                try {
                    int size = entityList.size();
                    String sqlStatement = this.sqlStatement(SqlMethod.INSERT_ONE);

                    for (int i = 0; i < size; ++i) {
                        batchSqlSession.insert(sqlStatement, entityList.get(i));
                        if (i >= 1 && i % batchSize == 0) {
                            batchSqlSession.flushStatements();
                        }
                    }

                    batchSqlSession.flushStatements();
                } catch (Throwable var16) {
                    var4 = var16;
                    throw var16;
                } finally {
                    if (batchSqlSession != null) {
                        if (var4 != null) {
                            try {
                                batchSqlSession.close();
                            } catch (Throwable var15) {
                                var4.addSuppressed(var15);
                            }
                        } else {
                            batchSqlSession.close();
                        }
                    }

                }

                return true;
            } catch (Throwable var18) {
                throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", var18);
            }
        }
    }
    //endregion

    //region 增加或更新

    /**
     * 插入或更新,
     * 不存在则插入,存在则更新
     */
    @Transactional(rollbackFor = {Exception.class})
    public boolean insertOrUpdate(T entity) {
        if (null == entity) {
            return false;
        } else {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return this.insert(entity);
                } else {
                    return this.updateById(entity) || this.insert(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertOrUpdateAllColumn(T entity) {
        if (null == entity) {
            return false;
        } else {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return this.insertAllColumn(entity);
                } else {
                    return this.updateAllColumnById(entity) || this.insertAllColumn(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertOrUpdateBatch(List<T> entityList) {
        return this.insertOrUpdateBatch(entityList, 30);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
        return this.insertOrUpdateBatch(entityList, batchSize, true);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertOrUpdateAllColumnBatch(List<T> entityList) {
        return this.insertOrUpdateBatch(entityList, 30, false);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertOrUpdateAllColumnBatch(List<T> entityList, int batchSize) {
        return this.insertOrUpdateBatch(entityList, batchSize, false);
    }

    private boolean insertOrUpdateBatch(List<T> entityList, int batchSize, boolean selective) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        } else {
            try {
                SqlSession batchSqlSession = this.sqlSessionBatch();
                Throwable var5 = null;

                try {
                    int size = entityList.size();
                    for (int i = 0; i < size; ++i) {
                        if (selective) {
                            this.insertOrUpdate(entityList.get(i));
                        } else {
                            this.insertOrUpdateAllColumn(entityList.get(i));
                        }
                        if (i >= 1 && i % batchSize == 0) {
                            batchSqlSession.flushStatements();
                        }
                    }
                    batchSqlSession.flushStatements();
                    return true;
                } catch (Throwable var16) {
                    var5 = var16;
                    throw var16;
                } finally {
                    if (batchSqlSession != null) {
                        if (var5 != null) {
                            try {
                                batchSqlSession.close();
                            } catch (Throwable var15) {
                                var5.addSuppressed(var15);
                            }
                        } else {
                            batchSqlSession.close();
                        }
                    }
                }
            } catch (Throwable var18) {
                throw new MybatisPlusException("Error: Cannot execute insertOrUpdateBatch Method. Cause", var18);
            }
        }
    }
    //endregion

    //region 删除
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteById(Serializable id) {
        return SqlHelper.delBool(this.baseMapper.deleteById(id));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteByMap(Map<String, Object> columnMap) {
        if (MapUtils.isEmpty(columnMap)) {
            throw new MybatisPlusException("deleteByMap columnMap is empty.");
        } else {
            return SqlHelper.delBool(this.baseMapper.deleteByMap(columnMap));
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(Wrapper<T> wrapper) {
        return SqlHelper.delBool(this.baseMapper.delete(wrapper));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteBatchIds(Collection<? extends Serializable> idList) {
        return SqlHelper.delBool(this.baseMapper.deleteBatchIds(idList));
    }
    //endregion

    //region 修改
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateById(T entity) {
        return retBool(this.baseMapper.updateById(entity));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean updateAllColumnById(T entity) {
        return retBool(this.baseMapper.updateAllColumnById(entity));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean update(T entity, Wrapper<T> wrapper) {
        return retBool(this.baseMapper.update(entity, wrapper));
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean updateBatchById(List<T> entityList) {
        return this.updateBatchById(entityList, 30);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean updateBatchById(List<T> entityList, int batchSize) {
        return this.updateBatchById(entityList, batchSize, true);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean updateAllColumnBatchById(List<T> entityList) {
        return this.updateAllColumnBatchById(entityList, 30);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean updateAllColumnBatchById(List<T> entityList, int batchSize) {
        return this.updateBatchById(entityList, batchSize, false);
    }

    private boolean updateBatchById(List<T> entityList, int batchSize, boolean selective) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        } else {
            try {
                SqlSession batchSqlSession = this.sqlSessionBatch();
                Throwable var5 = null;

                try {
                    int size = entityList.size();
                    SqlMethod sqlMethod = selective ? SqlMethod.UPDATE_BY_ID : SqlMethod.UPDATE_ALL_COLUMN_BY_ID;
                    String sqlStatement = this.sqlStatement(sqlMethod);

                    for (int i = 0; i < size; ++i) {
                        MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                        param.put("et", entityList.get(i));
                        batchSqlSession.update(sqlStatement, param);
                        if (i >= 1 && i % batchSize == 0) {
                            batchSqlSession.flushStatements();
                        }
                    }

                    batchSqlSession.flushStatements();
                    return true;
                } catch (Throwable var19) {
                    var5 = var19;
                    throw var19;
                } finally {
                    if (batchSqlSession != null) {
                        if (var5 != null) {
                            try {
                                batchSqlSession.close();
                            } catch (Throwable var18) {
                                var5.addSuppressed(var18);
                            }
                        } else {
                            batchSqlSession.close();
                        }
                    }
                }
            } catch (Throwable var21) {
                throw new MybatisPlusException("Error: Cannot execute updateBatchById Method. Cause", var21);
            }
        }
    }
    //endregion

    //region 查询
    //region 查询单一对象
    public T selectById(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    public T selectOne(Wrapper<T> wrapper) {
        return SqlHelper.getObject(this.baseMapper.selectList(wrapper));
    }

    public T selectOne(T entity) {
        return this.baseMapper.selectOne(entity);
    }

    public int selectCount(Wrapper<T> wrapper) {
        return SqlHelper.retCount(this.baseMapper.selectCount(wrapper));
    }

    public Object selectObj(Wrapper<T> wrapper) {
        return SqlHelper.getObject(this.baseMapper.selectObjs(wrapper));
    }
    //endregion

    //region 查询返回List
    public List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        return this.baseMapper.selectBatchIds(idList);
    }

    public List<T> selectByMap(Map<String, Object> columnMap) {
        return this.baseMapper.selectByMap(columnMap);
    }

    public List<T> selectList(Wrapper<T> wrapper) {
        return this.baseMapper.selectList(wrapper);
    }

    //endregion

    //region 看不懂的
    public List<Object> selectObjs(Wrapper<T> wrapper) {
        return this.baseMapper.selectObjs(wrapper);
    }

    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        return this.baseMapper.selectMaps(wrapper);
    }

    public Map<String, Object> selectMap(Wrapper<T> wrapper) {
        return SqlHelper.getObject(this.baseMapper.selectMaps(wrapper));
    }
    //endregion

    //region 查询得到Page


    @Override
    public Page<T> selectPage(Page<T> page) {
        return null;
    }

    @Override
    public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
        return null;
    }

    public MyPage<T> selectPage(MyPage<T> page) {
        return this.selectPage(page, Condition.EMPTY);
    }

    public MyPage<T> selectPage(MyPage<T> page, Wrapper<T> wrapper) {
        page.setRecords(this.baseMapper.selectPage(page, (Wrapper<T>) SqlHelper.fillWrapper(page, wrapper)));
        return page;
    }

    public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
        return page.setRecords(this.baseMapper.selectMapsPage(page, (Wrapper<T>) SqlHelper.fillWrapper(page, wrapper)));
    }
    //endregion

    //endregion

    //endregion

}