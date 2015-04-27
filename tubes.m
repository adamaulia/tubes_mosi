clear;
clc;

%%baca dataset 
data_training = xlsread('table.xlsx',2,'M23:O625');

data_testing = xlsread('table.xlsx',2,'M1:O24');

%% buat jaringan 

x1 = transpose(data_training(:,1));
x2 = transpose(data_training(:,2));
y = transpose(data_training(:,3));
input = [x1;x2];
p = [x1;x2]/10; 
t = y/10; 


net=newff(minmax(input),[3,1],{'tansig','tansig'},'traingd');


%%bobot
net.b{1} = [8.3971 ; 5.5116 ; 2.2688];
net.b{2} = [-0.6024];
net.IW{1,1} = [-0.3914 -0.4339 ; -0.2205 -0.5411; 0.4238 -0,4022 ];
net.LW{2,1} = [-0.6026 0.2076 -0.4556];



%%parameter trainnig
net.trainParam.show = 10;
net.trainParam.lr=0.01;
net.trainParam.epochs= 1000;
net.trainParam.goal=1e-5;


