package com.huiyuan2.demo.controller;

import com.huiyuan2.cloud.common.api.CommonResult;
import com.huiyuan2.demo.domain.DemoStaff;
import com.huiyuan2.demo.service.DemoStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 21:31
 */
@RestController
@RequestMapping("/demo/staff")
public class DemoStaffController {

    @Autowired
    private DemoStaffService demoStaffService;

    @PostMapping
    public CommonResult<Long> add(@RequestBody DemoStaff demoStaff){
       demoStaffService.getBaseMapper().insert(demoStaff);
       return CommonResult.success(demoStaff.getId());
    }
}
