import request from '@/utils/request'

// 查询景区列表
export function listSpot(query) {
  return request({
    url: '/system/spot/list',
    method: 'get',
    params: query
  })
}

// 查询景区详细
export function getSpot(scenicid) {
  return request({
    url: '/system/spot/' + scenicid,
    method: 'get'
  })
}

// 新增景区
export function addSpot(data) {
  return request({
    url: '/system/spot',
    method: 'post',
    data: data
  })
}

// 修改景区
export function updateSpot(data) {
  return request({
    url: '/system/spot',
    method: 'put',
    data: data
  })
}

// 删除景区
export function delSpot(scenicid) {
  return request({
    url: '/system/spot/' + scenicid,
    method: 'delete'
  })
}

// 导出景区
export function exportSpot(query) {
  return request({
    url: '/system/spot/export',
    method: 'get',
    params: query
  })
}