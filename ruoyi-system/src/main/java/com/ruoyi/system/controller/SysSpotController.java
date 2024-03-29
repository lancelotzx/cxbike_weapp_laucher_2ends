package com.ruoyi.system.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysSpot;
import com.ruoyi.system.service.ISysSpotService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * 景区Controller
 * 
 * @author rf
 * @date 2021-06-02
 */
@Api("畅行景区管理")
@RestController
@RequestMapping("/system/spot")
public class SysSpotController extends BaseController
{
    @Autowired
    private ISysSpotService sysSpotService;

    /**
     * 查询景区列表
     */
    @ApiOperation("获取景区列表")
    @PreAuthorize("@ss.hasPermi('system:spot:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSpot sysSpot)
    {
        startPage();
        List<SysSpot> list = sysSpotService.selectSysSpotList(sysSpot);
        return getDataTable(list);
    }

    /**
     * 导出景区列表
     */
    @ApiOperation("导出景区列表")
    @PreAuthorize("@ss.hasPermi('system:spot:export')")
    @Log(title = "景区", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysSpot sysSpot)
    {
        List<SysSpot> list = sysSpotService.selectSysSpotList(sysSpot);
        ExcelUtil<SysSpot> util = new ExcelUtil<SysSpot>(SysSpot.class);
        return util.exportExcel(list, "景区数据");
    }

    /**
     * 获取景区详细信息
     */
    @ApiOperation("获取景区详细信息")
    @PreAuthorize("@ss.hasPermi('system:spot:query')")
    @GetMapping(value = "/{scenicid}")
    public AjaxResult getInfo(@PathVariable("scenicid") String scenicid)
    {
        return AjaxResult.success(sysSpotService.selectSysSpotById(scenicid));
    }

    /**
     * 新增景区
     */
    @ApiOperation("新增景区")
    @PreAuthorize("@ss.hasPermi('system:spot:add')")
    @Log(title = "景区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSpot sysSpot)
    {
        int row = 0;
        try {
            row = sysSpotService.insertSysSpot(sysSpot);
            return toAjax(row);
        }catch (Exception e){
            return toAjax(0);
        }


    }

    /**
     * 修改景区
     */
    @ApiOperation("修改景区")
    @PreAuthorize("@ss.hasPermi('system:spot:edit')")
    @Log(title = "景区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSpot sysSpot)
    {
        return toAjax(sysSpotService.updateSysSpot(sysSpot));
    }

    /**
     * 删除景区
     */
    @ApiOperation("删除景区")
    @PreAuthorize("@ss.hasPermi('system:spot:remove')")
    @Log(title = "景区", businessType = BusinessType.DELETE)
	@DeleteMapping("/{scenicids}")
    public AjaxResult remove(@PathVariable String[] scenicids)
    {
        return toAjax(sysSpotService.deleteSysSpotByIds(scenicids));
    }
}
