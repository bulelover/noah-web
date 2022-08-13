/**
* ${tableDesc!}-API
*/
import http from "@/utils/http";
export default {
  getById: (config) => { return http._get('${requestMapping!}/getById', config) }, //根据ID查询${tableDesc!}
  page: (config) => { return http._post('${requestMapping!}/page', config) }, //${tableDesc!}分页列表查询
  add: (config) => { return http._post('${requestMapping!}/save', config) }, //新增${tableDesc!}信息
  edit: (config) => { return http._put('${requestMapping!}/save', config) }, //修改${tableDesc!}信息
  removeById: (config) => { return http._delete('${requestMapping!}/removeById', config) }, //删除${tableDesc!}
}