<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img src="../assets/img/header.jpg" class="user-avator" alt />
                        <div class="user-info-cont">
                            <div class="user-info-name">{{ name }}</div>
                            <div>{{ role }}</div>
                        </div>
                    </div>
                    <div class="user-info-list">
                        上次登录时间：
                        <span>2020-11-01</span>
                    </div>
                    <div class="user-info-list">
                        上次登录地点：
                        <span>杭州</span>
                    </div>
                </el-card>
                <el-card shadow="hover" style="height:252px;">
                    <template #header>
                      <el-container>
                        <div class="clearfix">
                            <span>个人资料</span>
                        </div>
                        <div class="right-items"  style="float: right;">
                          <el-button size="small" type="danger" plain>修改</el-button>
                          <el-button size="small" type="primary" plain>确认</el-button>
                        </div>
                      </el-container>
                    </template>
<!--                 增加个人资料部分-->
<!--                    <span>个人资料</span>-->
<!--                  </div>-->
                  <el-container>
                    <div class="Password" >
                      <span>昵称</span>
                    </div>
                    <el-col :span="15">
                      <el-input placeholder="请输入密码" v-model="admin.username" show-password></el-input>
                      <p>{{userName}}</p>
                    </el-col>
                  </el-container>
                  <!-- -->
                  <el-container>
                    <div class="Password" >
                      <span>邮箱</span>
                    </div>
                    <el-col :span="15">
                      <el-input placeholder="请输入密码" v-model="admin.email" show-password></el-input>
                      <p>{{mailbox}}</p>
                    </el-col>
                  </el-container>
                  <!-- -->
                  <el-container>
                    <div class="Password" >
                      <span>密码</span>
                    </div>
                    <el-col :span="15">
                      <el-input placeholder="请输入密码" v-model="admin.pwd" show-password></el-input>
                      <p>{{msg}}</p>
                    </el-col>
                  </el-container>
                </el-card>
            </el-col>
          <el-col :span="16">
            <el-row :gutter="20" class="mgb20">
              <el-col :span="8">
                <el-card shadow="hover" style="height: 526px">
                  <div class="grid-content grid-con-1">
                    <i class="el-icon-time grid-con-icon"></i>
                    <div class="grid-cont-right">
                      <div class="grid-num">100</div>
                      <div>已上传</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" style="height: 526px">
                  <div class="grid-content grid-con-2">
                    <i class="el-icon-star-off grid-con-icon"></i>
                    <div class="grid-cont-right">
                      <div class="grid-num">2</div>
                      <div>已领取</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" style="height: 526px">
                  <div class="grid-content grid-con-3">
                    <i class="el-icon-bell grid-con-icon"></i>
                    <div class="grid-cont-right">
                      <div class="grid-num">5</div>
                      <div>已发布</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
    </div>
</template>

<script>
import Schart from "vue-schart";
import { reactive } from "vue";
export default {
    name: "dashboard",
    components: { Schart },
    setup() {
        const name = localStorage.getItem("ms_username");
        const role = name === "admin" ? "超级管理员" : "普通用户";
        const admin = reactive({
            username: "admin",
            email: "Admin",
            pwd: "admin",
          });
        const data = reactive([
            {
                name: "2018/09/04",
                value: 1083,
            },
            {
                name: "2018/09/05",
                value: 941,
            },
            {
                name: "2018/09/06",
                value: 1139,
            },
            {
                name: "2018/09/07",
                value: 816,
            },
            {
                name: "2018/09/08",
                value: 327,
            },
            {
                name: "2018/09/09",
                value: 228,
            },
            {
                name: "2018/09/10",
                value: 1065,
            },
        ]);
        const options = {
        };
        const options2 = {

        };
        const todoList = reactive([
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: false,
            },
            {
                title: "今天要修复100个bug",
                status: true,
            },
            {
                title: "今天要写100行代码加几个bug吧",
                status: true,
            },
        ]);

        return {
            admin,
            name,
            data,
            options,
            options2,
            todoList,
            role,
        };
    },
};
</script>

<style scoped>
.el-row {
    margin-bottom: 20px;
}
.clearfix{
  text-align: left;
  margin-top: 2%;
}
.right-items{
  margin-left:150px;
  font-size: 10px;
  text-align: right;
}
.Password{
  font-size: 17px;
  margin-top: 2%;
}
.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}


</style>
