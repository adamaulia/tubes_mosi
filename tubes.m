clear;
clc;

%%baca dataset 
data_training = xlsread('table.xlsx',2,'M23:O625');

data_testing = xlsread('table.xlsx',2,'M1:O24');

%% buat jaringan 

x1 = transpose(data_training(:,1));
x2 = transpose(data_training(:,2));
ty = transpose(data_training(:,3));
input = [x1;x2];
p = [x1;x2]; 
t = ty; 


%net=newff(minmax(input),[3,1],{'tansig','tansig'},'traingd');
net=newff(p,t,[3 1]);

%%bobot
net.b{1} = [8.3971 ; 5.5116 ; 2.2688];
net.b{2} = [-0.6024];
net.IW{1,1} = [-0.3914 -0.4339 ; -0.2205 -0.5411; 0.4238 -0.4022 ];
net.LW{2,1} = [-0.6026 0.2076 -0.4556];



%%parameter trainnig
net.trainParam.show = 10;
net.trainParam.lr=0.01;
net.trainParam.epochs= 100;
net.trainParam.goal=1e-5;
[net,tr]=train(net,p,t);

%testing 
%========== 
%testing_x1 = transpose(data_testing(:,1)); 
%testing_x2 = transpose(data_testing(:,2)); 
%testing_y = transpose(data_testing(:,3)) ; 
%testing_input = [testing_x1;testing_x2]; 

%prediksi  = sim(net, testing_input); 
%[y,Pf,Af,e,perf]=sim(net,testing_input,[],[],testing_y); 
%prediksi = y; 
%aktual = testing_y; 

%error_prediksi = aktual - prediksi; 
%error_testing = mse(error_prediksi); 
% error_testing=sqrt(sum((aktual-prediksi)'*(aktual-prediksi))/length(aktual)); 

%figure(2) 

%plot(1:size(aktual,2),aktual,'o',1:size(prediksi,2),prediksi,'*') 
%xlabel('index record data (time series)');ylabel('nilai saham'); 
%legend('Data Aktual', 'Peramalan Backpropagation'); 
%grid on

%figure(3) 
%plot(1:size(error_prediksi,2),error_prediksi,'-')  
%title('Error peramalan') 
%xlabel('index record data (time series)');ylabel('mse'); 
%grid on

%disp('Performansi Jaringan = '); 
%disp(perf);

