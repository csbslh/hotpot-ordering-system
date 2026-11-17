/**
 * 文件说明：桌台业务层。
 * 作用：校验桌台参数，并处理桌台列表、新增和状态修改业务。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.DiningTable;
import com.cs.cs_hotpot_ordering_system.mapper.DiningTableMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiningTableService {
    private final DiningTableMapper mapper;
    public DiningTableService(DiningTableMapper mapper) { this.mapper = mapper; }
    /** 查询全部桌台。 */
    public List<DiningTable> list() { return mapper.findAll(); }
    /** 校验桌号和座位数后新增桌台。 */
    public void add(DiningTable table) { if (table.getTableNumber() == null || table.getSeatNumber() == null) throw new IllegalArgumentException("桌号和座位数不能为空"); mapper.insert(table); }
    /** 只允许使用系统规定的三种桌台状态。 */
    public void updateStatus(Long id, String status) { if (!List.of("AVAILABLE","OCCUPIED","RESERVED").contains(status)) throw new IllegalArgumentException("桌台状态不正确"); mapper.updateStatus(id,status); }
}
