package com.meiguo.information.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meiguo.common.utils.PageUtils;
import com.meiguo.common.utils.Query;
import com.meiguo.common.utils.R;
import com.meiguo.information.domain.RepairDO;
import com.meiguo.information.service.RepairService;

/**
 * 维修表
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2018-05-04 14:12:24
 */
 
@Controller
@RequestMapping("/information/repair")
public class RepairController {
	@Autowired
	private RepairService repairService;
	
	@GetMapping()
	@RequiresPermissions("information:repair:repair")
	String Repair(){
	    return "information/repair/repair";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("information:repair:repair")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String,Object>> repairList = repairService.list(query);
		int total = repairService.count(query);
		PageUtils pageUtils = new PageUtils(repairList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("information:repair:add")
	String add(){
	    return "information/repair/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("information:repair:edit")
	String edit(@PathVariable("id") Long id,Model model){
		Map<String,Object> repair = repairService.getRepair(id);
		model.addAttribute("repair", repair);
	    return "information/repair/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("information:repair:add")
	public R save( RepairDO repair){
		if(repairService.save(repair)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("information:repair:edit")
	public R update( RepairDO repair){
		//状态:0：申请、待报价；1：代付款；2：待评价；3：已完成；5：删除
		repairService.update(repair);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("information:repair:remove")
	public R remove( Long id){
		if(repairService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("information:repair:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		repairService.batchRemove(ids);
		return R.ok();
	}
	
}
