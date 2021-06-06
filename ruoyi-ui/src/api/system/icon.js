import request from '@/utils/request'

// 查询图标列表
export function listIcon(query) {
  return request({
    url: '/system/icon/list',
    method: 'get',
    params: query
  })
}

// 查询图标详细
export function getIcon(iconid) {
  return request({
    url: '/system/icon/' + iconid,
    method: 'get'
  })
}

// 新增图标
export function addIcon(data) {
  return request({
    url: '/system/icon',
    method: 'post',
    data: data
  })
}

// 修改图标
export function updateIcon(data) {
  return request({
    url: '/system/icon',
    method: 'put',
    data: data
  })
}

// 删除图标
export function delIcon(iconid) {
  return request({
    url: '/system/icon/' + iconid,
    method: 'delete'
  })
}

// 导出图标
export function exportIcon(query) {
  return request({
    url: '/system/icon/export',
    method: 'get',
    params: query
  })
}

//图标上传到腾讯云COS的前端接口
// 2级icon上传
export function uploadIcon(data) {
  return request({
    url: '/system/icon/upload',
    method: 'post',
    data: data
  })
}