package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysIconMapper;
import com.ruoyi.system.mapper.SysLv3listMapper;
import com.ruoyi.system.mapper.SysSpotMapper;
import com.ruoyi.system.service.ISysIconService;
import com.ruoyi.system.service.ISysLv3listService;
import com.ruoyi.system.service.ISysSpotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.system.service.ISysSpotTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.validation.constraints.Null;


/**
 * 模版管理Controller
 * 
 * @author RF
 * @date 2021-06-13
 */
@Api("模版管理")
@RestController
@RequestMapping("/system/template")
public class SysSpotTemplateController extends BaseController
{
    @Autowired
    private ISysSpotTemplateService sysSpotTemplateService;

    @Autowired
    private ISysSpotService sysSpotService;

    @Autowired
    private ISysIconService sysIconService;

    @Autowired
    private ISysLv3listService sysLv3listService;

    @Autowired
    private SysSpotMapper sysSpotMapper;

    @Autowired
    private SysIconMapper sysIconMapper;

    @Autowired
    private SysLv3listMapper sysLv3listMapper;

    /**
     * 查询模版管理列表
     */
    @ApiOperation("查询模版列表")
    @PreAuthorize("@ss.hasPermi('system:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSpotTemplate sysSpotTemplate)
    {
        startPage();
        List<SysSpotTemplate> list = sysSpotTemplateService.selectSysSpotTemplateList(sysSpotTemplate);
        return getDataTable(list);
    }

    /**
     * 导出模版管理列表
     */
    @ApiOperation("导出模版列表")
    @PreAuthorize("@ss.hasPermi('system:template:export')")
    @Log(title = "模版管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysSpotTemplate sysSpotTemplate)
    {
        List<SysSpotTemplate> list = sysSpotTemplateService.selectSysSpotTemplateList(sysSpotTemplate);
        ExcelUtil<SysSpotTemplate> util = new ExcelUtil<SysSpotTemplate>(SysSpotTemplate.class);
        return util.exportExcel(list, "模版管理数据");
    }

    /**
     * 获取模版管理详细信息
     */
    @ApiOperation("获取模版详细信息")
    @PreAuthorize("@ss.hasPermi('system:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysSpotTemplateService.selectSysSpotTemplateById(id));
    }

    /**
     * 新增模版管理
     */
    @ApiOperation("新增模版管理")
    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @Log(title = "模版管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSpotTemplate sysSpotTemplate)
    {
        // return toAjax(sysSpotTemplateService.insertSysSpotTemplate(sysSpotTemplate));
        int row = 0;
        try {
            row = sysSpotTemplateService.insertSysSpotTemplate(sysSpotTemplate);
            return toAjax(row);
        }catch (Exception e){
            return toAjax(0);
        }
    }

    /**
     * 通过模版增加数据管理
     */
    @ApiOperation("通过模版生成数据，参数为新景区名称和模版内景区id")
    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @Log(title = "模版生成数据", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult add2(@RequestBody SysSpotGen sysSpotGen)
    {
        boolean ret;
        int row = 0;
        System.out.println(sysSpotGen.getScenicid() + ',' + sysSpotGen.getNewSpotName() + ',' +
                sysSpotGen.getNewSenicId());
        // 这里进行数据处理
        ret = buildMultiDataFromTemplate(sysSpotGen.getScenicid(), sysSpotGen.getNewSpotName(),
                sysSpotGen.getNewSenicId());
        if(ret == true)
            return toAjax(1);
        else
            return toAjax(0);
    }

    // 批量生成数据的业务处理,
    // 参数1：旧景区id，参数2：新景区名称，录入；参数3：新景区id，录入
    //Step1.先获取old景区的数据并插入到sys_spot表
    //Step2.再获取old景区的icon数据并插入到sys_icon表
    boolean buildMultiDataFromTemplate(String oldScenicId,
                                       String newSpotName,
                                       String newScenicId
                                       ){

        //Step1 Start 景区级别的复制
        SysSpot sysSpotNew =  sysSpotService.selectSysSpotById(oldScenicId);
        // String newScenicId = sysSpotNew.getScenicid() + '_' + IdUtils.simpleUUID();
        //下面sysSpotNew中的iconserial是oldspot中的，是无效的，需要在icon级别插入后更新new数据。
        sysSpotNew.setScenicid(newScenicId);
        sysSpotNew.setName(newSpotName);
        int row = 0;
        String newiconserial ;
        HashMap<Long, String> map ;
        String oldiconserial = sysSpotNew.getIconserial();
        //newiconserial = getNewSerialIconString(oldiconserial);
        try {
            //Step2
            map = insertNewSysIcon(oldScenicId, newScenicId);
            newiconserial = getNewSerialIconString(oldiconserial, map);
            sysSpotNew.setIconserial(newiconserial);
            sysSpotMapper.insertSysSpot(sysSpotNew);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    //传递一个模版中spot的字符串，得到当前生成的最后一次克隆数据的新字符串
    //参数1 oldSerialIconString 模版景区的原有顺序编号
    //参数2 通过lv2 拷贝得到的新插入到icon表中的map
    //return : 通过排序后的新的iconserial排序字符串，用于写入到spot表中的对应数据
    public String getNewSerialIconString(
            String oldSerialIconString,
            HashMap<Long, String> map
            )
    {
        StringBuffer sb = new StringBuffer();
       // List<String> oldIconNameList = new ArrayList<String>();
        for(String s : oldSerialIconString.split(",")){
            //通过iconid在icon表中找到中文名称
            Long temp_id = Long.valueOf(s);
            SysIcon icon = sysIconMapper.selectSysIconById(temp_id);
            for(Long id: map.keySet()){
                if(icon.getIconname().equals(map.get(id))){
                    sb.append(String.valueOf(id));
                    sb.append(",");
                }
            }
        }
        // 通过nameList再去给刚插入icon表的icon进行排序，得到新的Serial
       String ret = sb.toString();
        if(ret.length() > 0){
            ret = ret.substring(0, ret.length() - 1);
        }

        return ret;
    }

    //Step2 中复制的实现，其中也包括Step3级别的实现
    //从oldSpotId对应的spot中获取icons并填充到icon表中，icon的scenicid为newSpotId
    //注意，必须要新增到icon表中，id是自增的，不能指定。
    //注意，此处需要通过mybatis的feature获取到刚刚新增的iconid
    //参数1：旧景区id 参数2：新景区id，用于写入daoicon表
    //返回值：一个包含<newIconId, newIconName>的map
    public HashMap<Long,String> insertNewSysIcon(
            String oldScenicId,
            String newScenicId)
    {
        SysSpot sysSpot = sysSpotService.selectSysSpotById(oldScenicId);
        List<SysIcon> sysIconList = sysSpot.getSysIconList();
        String scenicid = newScenicId;
        HashMap<Long, String> map = new HashMap<Long, String>();
        if (StringUtils.isNotNull(sysIconList))
        {
            for (SysIcon sysIcon : sysIconList)
            {
                sysIcon.setScenicid(scenicid);
                long oldiconid = sysIcon.getIconid();
                sysIcon.cleanIconid();
                //sysIconService.insertSysIcon(sysIcon);
                sysIconMapper.insertSysIcon(sysIcon);
                //通过mybatis的feature获取到最后insert的自增iconid，用于后续lv3的新增
                Long iconid = sysIcon.getIconid();
                map.put(iconid, sysIcon.getIconname());
                //System.out.println(iconid);
                // Step3.lv3级别的复制，在lv2级别内实现
                insertNewSysLv3(oldiconid, iconid);
            }
        }
        return map;
    }

    //从oldSpotId对应的spot中获取icons并填充到icon表中，由于从icon级别开始的id都是自动
    //因此参数只需要旧iconid即可
    public void insertNewSysLv3(Long oldIconId, Long newIconId)
    {
        SysIcon sysIcon = sysIconService.selectSysIconById(oldIconId);
        List<SysLv3list> sysLv3listList = sysIcon.getSysLv3listList();
        if (StringUtils.isNotNull(sysLv3listList))
        {
            for (SysLv3list sysLv3list : sysLv3listList)
            {
                //这里的iconid需要是之前2级生成的iconid
                sysLv3list.setIconid(newIconId);
                sysLv3list.cleanid();
                //sysIconService.insertSysIcon(sysIcon);
                sysLv3listMapper.insertSysLv3list(sysLv3list);
            }
        }
    }



    /**
     * 修改模版管理
     */
    @ApiOperation("修改模版数据")
    @PreAuthorize("@ss.hasPermi('system:template:edit')")
    @Log(title = "模版管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSpotTemplate sysSpotTemplate)
    {
       // return toAjax(sysSpotTemplateService.updateSysSpotTemplate(sysSpotTemplate));
        int row = 0;
        try {
            row = sysSpotTemplateService.updateSysSpotTemplate(sysSpotTemplate);
            return toAjax(row);
        }catch (Exception e){
            return toAjax(0);
        }
    }

    /**
     * 删除模版管理
     */
    @ApiOperation("删除模版数据")
    @PreAuthorize("@ss.hasPermi('system:template:remove')")
    @Log(title = "模版管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysSpotTemplateService.deleteSysSpotTemplateByIds(ids));
    }
}
