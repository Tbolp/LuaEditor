<template>
  <div class="app">
    <el-tree
      style="width: fit-content; min-width: 100%"
      :props="props"
      :load="loadNode"
      :indent="6"
      @node-expand="handleexpand"
      @node-collapse="handlecollapse"
      @current-change="setCurrentPath"
      lazy
      highlight-current
    ></el-tree>
  </div>
</template>
<script lang="ts">
import { Resolve } from "element-ui/types/cascader-panel";
import { TreeData, TreeNode } from "element-ui/types/tree";
import Vue from "vue";
// 测试使用的接口
import { API } from "./androidAPIMock";
// 发布时的接口
// import { androidAPI } from "./androidAPI";
// declare var API: androidAPI;
export default Vue.extend({
  data: () => {
    return {
      props: { label: "name", children: "zones", isLeaf: "leaf" },
    };
  },
  methods: {
    handleexpand(data: any, node: TreeNode<any, any>) {
      // console.log(data.leaf);
      // console.log(node.isLeaf);
      node.isLeaf = data.leaf;
    },
    handlecollapse(data: TreeData, node: TreeNode<any, any>) {
      node.loaded = false;
      // console.log(node);
    },
    loadNode(node: TreeNode<any, any>, resolve: Resolve<any>) {
      // console.log(node);
      if (node.level === 0) {
        return resolve([{ name: this.getRoot(), leaf: false }]);
      }
      var path = this.getFullPath(node);
      if (node.level > 5) {
        return resolve([]);
      } else {
        return resolve(this.listFile(path));
      }
    },
    getFullPath(node: TreeNode<any, any>): string {
      var path = "";
      var tmp_node = node;
      while (tmp_node.level != 1) {
        path = tmp_node.label + "/" + path;
        tmp_node = tmp_node.parent!;
      }
      if (path === "") {
        path = this.getRootFullPath();
      } else {
        path = this.getRootFullPath() + "/" + path;
      }
      return path;
    },
    getRoot(): string {
      return API.getRoot();
    },
    getRootFullPath(): string {
      return API.getRootFullPath();
    },
    listFile(path: string) {
      return JSON.parse(API.listFile(path));
    },
    setCurrentPath(data: TreeData, node: TreeNode<any, any>) {
      console.log(node);
      API.setCurrentPath(this.getFullPath(node));
    },
  },
});
</script>
<style scoped>
app {
  overflow: auto;
}
</style>
