<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${cacheClassName}"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        t.${field.columnName},
</#list>
<#list table.fields as field>
    <#if field_index%8 == 0>
    </#if>
    <#if field_index == 0>t.${field.name}<#else >,t.${field.name}</#if>
</#list>
    </sql>

    <sql id="conditions">
        <where>
            t.flag = 1
            <if test="search != null and search != ''">
                -- and (t.code like ${r"#{"}search${r"}"}  or t.name like ${r"#{"}search${r"}"})
            </if>
            <#list table.fields as field>
            <#if field_index gt 0 && field_index lt 4>
            <if test="${field.propertyName} != null and ${field.propertyName} != ''">
                and t.${field.name} = ${r"#{"}${field.propertyName}${r"}"}
            </if>
            </#if>
            </#list>
        </where>
    </sql>

    <select id="selectPageList" resultType="${package.Entity}.${entity}">
        select <include refid="Base_Column_List"/> from ${schemaName}${table.name} t
        <include refid="conditions"/>
        ${r"${orderSql}"}
    </select>

</#if>
</mapper>
