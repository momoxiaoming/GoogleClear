
als-gen.exe -c .\als-app.toml -t .\umeng-android-kt.tmpl --out-base-dir ../src/main/java -o St.kt

:: 生成umeng的参数列表，用于导入结果
als-gen.exe -c .\als-app.toml -t .\umeng-add.tmpl -o St-add.txt -use-pkg=0
