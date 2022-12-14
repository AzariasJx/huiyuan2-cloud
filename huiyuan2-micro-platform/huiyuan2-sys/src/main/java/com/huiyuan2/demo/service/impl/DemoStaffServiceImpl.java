package com.huiyuan2.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyuan2.demo.domain.DemoStaff;
import com.huiyuan2.demo.mapper.DemoStaffMapper;
import com.huiyuan2.demo.service.DemoStaffService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 21:36
 */
@Service
public class DemoStaffServiceImpl extends ServiceImpl<DemoStaffMapper, DemoStaff> implements DemoStaffService {
}
