package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.SysSpot;
import com.ruoyi.system.mapper.SysSpotMapper;
import com.ruoyi.system.service.ISysSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.SysLv3list;
import com.ruoyi.system.mapper.SysIconMapper;
import com.ruoyi.system.domain.SysIcon;
import com.ruoyi.system.service.ISysIconService;

/**
 * 图标Service业务层处理
 * 
 * @author rf
 * @date 2021-06-05
 */
@Service
public class SysIconServiceImpl implements ISysIconService 
{
    @Autowired
    private SysIconMapper sysIconMapper;

    @Autowired
    private ISysSpotService sysSpotService;

    @Autowired
    private SysSpotMapper sysSpotMapper;

    /**
     * 查询图标
     * 
     * @param iconid 图标ID
     * @return 图标
     */
    @Override
    public SysIcon selectSysIconById(Long iconid)
    {
        return sysIconMapper.selectSysIconById(iconid);
    }

    /**
     * 查询图标列表
     * 
     * @param sysIcon 图标
     * @return 图标
     */
    @Override
    public List<SysIcon> selectSysIconList(SysIcon sysIcon)
    {
        return sysIconMapper.selectSysIconList(sysIcon);
    }

    /**
     * 新增图标，注意，新增图标需要更新spot的iconserial字符串
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSysIcon(SysIcon sysIcon)
    {
        int rows = sysIconMapper.insertSysIcon(sysIcon);
        // wangjia:新增图标需要在spot的iconserail最后加上该图标的id
        Long iconid = sysIcon.getIconid();
        // update到spot表中
        SysSpot sysSpotNew =  sysSpotService.selectSysSpotById(sysIcon.getScenicid());
        if(sysSpotNew != null) {
            String is = sysSpotNew.getIconserial();
            is = is + "," + String.valueOf(iconid);
            sysSpotNew.setIconserial(is);
            sysSpotMapper.updateSysSpot(sysSpotNew);
        }
        // end by wangjia
        insertSysLv3list(sysIcon);

        return rows;
    }

    /**
     * 修改图标
     * 
     * @param sysIcon 图标
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSysIcon(SysIcon sysIcon)
    {
        sysIconMapper.deleteSysLv3listByIconid(sysIcon.getIconid());
        insertSysLv3list(sysIcon);
        return sysIconMapper.updateSysIcon(sysIcon);
    }

    /**
     * 批量删除图标
     * 
     * @param iconids 需要删除的图标ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSysIconByIds(Long[] iconids)
    {
        //wangjia:删除icon的时候需要同步删除spot中的iconserial中的id
        // 先找找spot对象，需要从iconid找到icon对象
        for(Long iconid: iconids){
            SysIcon si = selectSysIconById(iconid);
            SysSpot sysSpotNew =  sysSpotService.selectSysSpotById(si.getScenicid());
            if(sysSpotNew != null) {
                StringBuffer sb = new StringBuffer();
                String oldSerialIconString = sysSpotNew.getIconserial();
                // List<String> oldIconNameList = new ArrayList<String>();
                for(String s : oldSerialIconString.split(",")){
                    //通过iconid在iconserial中找
                    Long temp_id = Long.valueOf(s);
                    if(temp_id.longValue() == iconid.longValue()){
                        continue;
                    }
                    sb.append(String.valueOf(temp_id));
                    sb.append(",");
                }
                // 得到新的Serial，更新spot表
                String serialString = sb.toString();
                // 判断一下字符串是否','结尾，若是的话去掉
                if(serialString.length() > 0 && serialString.substring(serialString.length()-1).equals(",")){
                    serialString = serialString.substring(0, serialString.length() - 1);
                }
                sysSpotNew.setIconserial(serialString);

                sysSpotMapper.updateSysSpot(sysSpotNew);
            }
            // end add by wangjia
        }
        sysIconMapper.deleteSysLv3listByIconids(iconids);
        return sysIconMapper.deleteSysIconByIds(iconids);
    }

    /**
     * 删除图标信息，注意，删除图标需要判断spot的iconserial中是否存在，若存在也需要删除,
     * 还需要注意景区自身是否存在。
     * 
     * @param iconid 图标ID
     * @return 结果
     */
    @Override
    public int deleteSysIconById(Long iconid)
    {

        //wangjia:删除icon的时候需要同步删除spot中的iconserial中的id
        // 先找找spot对象，需要从iconid找到icon对象
        SysIcon si = selectSysIconById(iconid);
        SysSpot sysSpotNew =  sysSpotService.selectSysSpotById(si.getScenicid());
        if(sysSpotNew != null) {
            StringBuffer sb = new StringBuffer();
            String oldSerialIconString = sysSpotNew.getIconserial();
            // List<String> oldIconNameList = new ArrayList<String>();
            for(String s : oldSerialIconString.split(",")){
                //通过iconid在iconserial中找
                  Long temp_id = Long.valueOf(s);
                  if(temp_id.longValue() == iconid.longValue()){
                    continue;
                  }
                  sb.append(String.valueOf(temp_id));
                  sb.append(",");
            }
            // 得到新的Serial，更新spot表
            String serialString = sb.toString();
            // 判断一下字符串是否','结尾，若是的话去掉
            if(serialString.length() > 0 && serialString.substring(serialString.length()-1).equals(",")){
                serialString = serialString.substring(0, serialString.length() - 1);
            }
            sysSpotNew.setIconserial(serialString);
            sysSpotMapper.updateSysSpot(sysSpotNew);
        }
        // end add by wangjia
        sysIconMapper.deleteSysLv3listByIconid(iconid);
        return sysIconMapper.deleteSysIconById(iconid);
    }

    /**
     * 新增${subTable.functionName}信息
     * 
     * @param sysIcon 图标对象
     */
    public void insertSysLv3list(SysIcon sysIcon)
    {
        List<SysLv3list> sysLv3listList = sysIcon.getSysLv3listList();
        Long iconid = sysIcon.getIconid();
        if (StringUtils.isNotNull(sysLv3listList))
        {
            List<SysLv3list> list = new ArrayList<SysLv3list>();
            for (SysLv3list sysLv3list : sysLv3listList)
            {
                sysLv3list.setIconid(iconid);
                list.add(sysLv3list);
            }
            if (list.size() > 0)
            {
                sysIconMapper.batchSysLv3list(list);
            }
        }
    }
}
