<template>
  <el-container>
    <el-container>
      <el-aside width="60px">侧边栏
        <div class="EditButton1">
          <el-button size="small" type="danger" plain>修改</el-button>
        </div>
        <div class="EditButton2">
          <el-button size="small" type="warning" plain>修改</el-button>
        </div>
      </el-aside>
      <el-container>
        <el-main height="50px"  STYLE="height: 600px">
          <img src="../assets/img/mylogin.jpg" class="user-avator" alt />
        </el-main>
        <el-footer  height="50px">Footer</el-footer>
      </el-container>
      <el-aside width="100px"></el-aside>
    </el-container>
  </el-container>
</template>



<script>
export default {
}

</script>

<style>
.EditButton1 {
  margin-top: 200px;
}
.EditButton2 {
  margin-top: 2px;
}
</style>
