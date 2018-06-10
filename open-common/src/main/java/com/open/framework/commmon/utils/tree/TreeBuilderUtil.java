package com.open.framework.commmon.utils.tree;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilderUtil {

    List<TreeNodeDto> TreeNodeDTOs = new ArrayList<TreeNodeDto>();
    private String type;

    public TreeBuilderUtil(List<TreeNodeDto> TreeNodeDTOs) {
        super();
        this.TreeNodeDTOs = TreeNodeDTOs;
    }

    public TreeBuilderUtil(List<TreeNodeDto> TreeNodeDTOs, String type) {
        super();
        this.type = type;
        this.TreeNodeDTOs = TreeNodeDTOs;
    }

    /**
     * 构建JSON树形结构
     *
     * @return
     */
    public String buildJSONTree() {
        List<TreeNodeDto> nodeTree = buildTree();
        return JSON.toJSONString(nodeTree);
    }

    public List<TreeNodeDto> buildListTree() {
        List<TreeNodeDto> nodeTree = buildTree();
        return nodeTree;
    }

    /**
     * 构建树形结构
     *
     * @return
     */
    public List<TreeNodeDto> buildTree() {
        List<TreeNodeDto> treeTreeNodeDTOs = new ArrayList<TreeNodeDto>();
        List<TreeNodeDto> rootTreeNodeDTOs = getRootTreeNodeDTOs();
        for (TreeNodeDto rootTreeDTO : rootTreeNodeDTOs) {
            buildChildTreeNodeDTOs(rootTreeDTO);
            treeTreeNodeDTOs.add(rootTreeDTO);
        }
        return treeTreeNodeDTOs;
    }

    /**
     * 递归子节点
     *
     * @param TreeDTO
     */
    public void buildChildTreeNodeDTOs(TreeNodeDto TreeDTO) {
        List<TreeNodeDto> children = getChildTreeNodeDTOs(TreeDTO);
        if (!children.isEmpty()) {
            for (TreeNodeDto child : children) {
                buildChildTreeNodeDTOs(child);
            }
            if ("children".equals(type)) {
                TreeDTO.setChildren(children);
            } else {
                TreeDTO.setChilds(children);
            }
        }
    }

    /**
     * 获取父节点下所有的子节点
     *
     * @param TreeNodeDTOs
     * @param pTreeDTO
     * @return
     */
    public List<TreeNodeDto> getChildTreeNodeDTOs(TreeNodeDto pTreeDTO) {
        List<TreeNodeDto> childTreeNodeDTOs = new ArrayList<TreeNodeDto>();
        for (TreeNodeDto n : TreeNodeDTOs) {
            if (pTreeDTO.getId().equals(n.getPid())) {
                childTreeNodeDTOs.add(n);
            }
        }
        return childTreeNodeDTOs;
    }

    /**
     * 判断是否为根节点
     *
     * @param TreeNodeDTOs
     * @param inTreeDTO
     * @return
     */
    public boolean rootTreeDTO(TreeNodeDto TreeDTO) {
        boolean isRootTreeDTO = true;
        if (StringUtils.isNotBlank(TreeDTO.getPid())) {
            for (TreeNodeDto n : TreeNodeDTOs) {
                if (TreeDTO.getPid().equals(n.getId())) {
                    isRootTreeDTO = false;
                    break;
                }
            }
        }

        return isRootTreeDTO;
    }

    /**
     * 获取集合中所有的根节点
     *
     * @param TreeNodeDTOs
     * @return
     */
    public List<TreeNodeDto> getRootTreeNodeDTOs() {
        List<TreeNodeDto> rootTreeNodeDTOs = new ArrayList<TreeNodeDto>();
        for (TreeNodeDto n : TreeNodeDTOs) {
            if (rootTreeDTO(n)) {
                rootTreeNodeDTOs.add(n);
            }
        }
        return rootTreeNodeDTOs;
    }
}
