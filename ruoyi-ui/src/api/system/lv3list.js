import request from '@/utils/request'

// 查询三级列表列表
export function listLv3list(query) {
  return request({
    url: '/system/lv3list/list',
    method: 'get',
    params: query
  })
}

// 查询三级列表详细
export function getLv3list(id) {
  return request({
    url: '/system/lv3list/' + id,
    method: 'get'
  })
}

// 新增三级列表
export function addLv3list(data) {
  return request({
    url: '/system/lv3list',
    method: 'post',
    data: data
  })
}

// 修改三级列表
export function updateLv3list(data) {
  return request({
    url: '/system/lv3list',
    method: 'put',
    data: data
  })
}

// 删除三级列表
export function delLv3list(id) {
  return request({
    url: '/system/lv3list/' + id,
    method: 'delete'
  })
}

// 导出三级列表
export function exportLv3list(query) {
  return request({
    url: '/system/lv3list/export',
    method: 'get',
    params: query
  })
}