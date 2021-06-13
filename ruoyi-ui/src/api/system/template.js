import request from '@/utils/request'

// 查询模版管理列表
export function listTemplate(query) {
  return request({
    url: '/system/template/list',
    method: 'get',
    params: query
  })
}

// 查询模版管理详细
export function getTemplate(id) {
  return request({
    url: '/system/template/' + id,
    method: 'get'
  })
}

// 新增模版管理
export function addTemplate(data) {
  return request({
    url: '/system/template',
    method: 'post',
    data: data
  })
}

// 修改模版管理
export function updateTemplate(data) {
  return request({
    url: '/system/template',
    method: 'put',
    data: data
  })
}

// 删除模版管理
export function delTemplate(id) {
  return request({
    url: '/system/template/' + id,
    method: 'delete'
  })
}

// 导出模版管理
export function exportTemplate(query) {
  return request({
    url: '/system/template/export',
    method: 'get',
    params: query
  })
}