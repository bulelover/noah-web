/**
* ${tableDesc!}-API
*/
import ajax from "@/utils/ajax";
export default {
  getById: (config) => { return ajax._get('${requestMapping!}/getById', config) }, //根据ID查询${tableDesc!}
  page: (config) => { return ajax._post('${requestMapping!}/page', config) }, //${tableDesc!}分页列表查询
  add: (config) => { return ajax._post('${requestMapping!}/save', config) }, //新增${tableDesc!}信息
  edit: (config) => { return ajax._put('${requestMapping!}/save', config) }, //修改${tableDesc!}信息
  removeById: (config) => { return ajax._delete('${requestMapping!}/removeById', config) }, //删除${tableDesc!}
}