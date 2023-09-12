# 从github发布aar到jitpack
1. 保证自己的github仓库有该库，
2. 注册jitpack.io，直接使用github账号绑定即可
3. 在jitpack.io中look up 自己的仓库
4. 点击下面的get it即可发布aar

# 从gitlab发布aar到jitPack

1. [获取accessToken教程](https://blog.csdn.net/qq_14997473/article/details/114117903)，从gitlab上生成[accessToken](https://gitlab.cz89.com/-/profile/personal_access_tokens),主要token生成后要及时保存本地。不然以后再也看不见
2. 在[jitpack](https://jitpack.io/w/user)网站注册,在[个人信息](https://jitpack.io/w/user)里的GitLab.com的personal Access Token 填写步骤1获取的accessToken
3. 在jitpack.io网站直接搜索com.cz89.gitlab.android_libs/loadsir,也可以直接打开该网址https://jitpack.io/#com.cz89.gitlab.android_libs/loadsir，点击Look up
4. 在下面的Releases、Builds、Branches、Commits直接点击Get it按钮就可直接发布aar到jitpack（建议：对指定提交记录打远程tag，然后在Releases选中该tag发布aar）