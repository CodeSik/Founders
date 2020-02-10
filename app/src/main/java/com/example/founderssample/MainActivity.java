package com.example.founderssample;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.ui.CucumberWebView;
import com.samsung.android.sdk.blockchain.ui.OnSendTransactionListener;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnSendTransactionListener {

    Button connectBtn;
    Button generateAccountBtn;
    Button getAccountsBtn;
    Button paymentSheetBtn;
    Button sendSmartContractBtn;
    Button webViewInitBtn;
    private SBlockchain sBlockchain;
    private HardwareWallet wallet;
    private Account generateAccount;
    private CucumberWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sBlockchain = new SBlockchain();
        try {
            sBlockchain.initialize(this);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }

        connectBtn = findViewById(R.id.connect);
        generateAccountBtn= findViewById(R.id.generateAccount);
        getAccountsBtn= findViewById(R.id.getAccounts);
        paymentSheetBtn= findViewById(R.id.paymentsheet);
        sendSmartContractBtn= findViewById(R.id.sendSmartContract);
        webViewInitBtn = findViewById(R.id.webViewInit);
        webView = findViewById(R.id.cucumberWebView);

        // 얘가 버튼 클릭했을 때 실행되는 코드
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        generateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate();
            }
        });

        getAccountsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccounts();
            }
        });

        paymentSheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSheet();
            }
        });

        sendSmartContractBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //나중에!!  sendSmartContract();
            }
        }));

        webViewInitBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewInit();
            }
        }));
    }


    private String add () {
        return "더하기";
    }
    private void webViewInit(){
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/8ed1117da130482fb477c9d47fff3043"
        );
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );
        //CoinServiceFactory는 이더리움에 관한 함수들의 집합체를 받을 수 있음.
        EthereumService ethereumService = (EthereumService) CoinServiceFactory
                .getCoinService(this, coinNetworkInfo );

        webView.init(ethereumService,accounts.get(0),this);
        webView.loadUrl("https://faucet.ropsten.be/");

    }
    /*
    private void sendSmartContract(){
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/8ed1117da130482fb477c9d47fff3043"
        );
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );
        EthereumService etherService = (EthereumService) CoinServiceFactory.getCoinService(this, coinNetworkInfo );
        //인텐트 관련 코드 추가 필요
        try {
            etherService
                    .sendSmartContractTransaction(
                            wallet,
                            (EthereumAccount) accounts.get(0),
                            "0xFab46E002BbF0b4509813474841E0716E6730136",
                            EthereumUtils.convertGweiToWei(new BigDecimal("10")),
                            new BigInteger("500000"),
                            add(),
                            null,
                            null
                    )
                    .setCallback(
                            new ListenableFutureTask.Callback<TransactionResult>() {
                                @Override
                                public void onSuccess(TransactionResult result) {
                                    //success
                                }
                                @Override
                                public void onFailure(ExecutionException exception) {
                                    //failure
                                }
                                @Override
                                public void onCancelled(InterruptedException exception) {
                                    //cancelled
                                }
                            });
        } catch (AvailabilityException e) {
            //handle exception
        }


    }
*/

    private void paymentSheet(){
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/8ed1117da130482fb477c9d47fff3043"
        );
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );
        //CoinServiceFactory는 이더리움에 관한 함수들의 집합체를 받을 수 있음.
        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(this, coinNetworkInfo );
        //트랜잭션에 데이터 넣어서 스마트컨트랙터 보낼 수 있음(String s1 파라미터)
        Intent intent = ethereumService
                .createEthereumPaymentSheetActivityIntent(
                        this,
                        wallet,
                        (EthereumAccount) accounts.get(0),
                         "0xedC1618A6DA5Bc98E08482189351C17B9aC45Dc4",
                        new BigInteger("1000000000000000"),
                        null,
                        null
                );
        startActivityForResult(intent,0);



    }
    private void getAccounts(){
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(wallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);
        Log.d("MyApp", Arrays.toString(new List[] {accounts}));
    }

    private void generate() {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/8ed1117da130482fb477c9d47fff3043"
                );

        sBlockchain.getAccountManager()
                .generateNewAccount(wallet, coinNetworkInfo)
                .setCallback(new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        generateAccount = account;
                        Log.d("MyApp",account.toString());
                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.d("MyApp" , e.toString());
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {

                    }
                });
    }
    private void connect() {
        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG ,true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet hardwareWallet) {
                        wallet =hardwareWallet;

                    }

                    @Override
                    public void onFailure(ExecutionException e) {

                    }

                    @Override
                    public void onCancelled(InterruptedException e) {

                    }
                });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSendTransaction(
            @NotNull String requestId,
            @NotNull EthereumAccount fromAccount,
            @NotNull String toAddress,
            @org.jetbrains.annotations.Nullable BigInteger value,
            @org.jetbrains.annotations.Nullable String data,
            @org.jetbrains.annotations.Nullable BigInteger nonce
    ) {
        HardwareWallet connectedHardwareWallet =
                sBlockchain.getHardwareWalletManager().getConnectedHardwareWallet();
        Intent intent =
                webView.createEthereumPaymentSheetActivityIntent(
                        this,
                        requestId,
                        connectedHardwareWallet,
                        toAddress,
                        value,
                        data,
                        nonce
                );

        startActivityForResult(intent, 0);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 0) {
            return;
        }

        webView.onActivityResult(requestCode, resultCode, data);
    }
}
