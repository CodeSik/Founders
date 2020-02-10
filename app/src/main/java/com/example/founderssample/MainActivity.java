package com.example.founderssample;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ledger.lib.apps.eth.Eth;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SNIHostName;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    Button connectBtn;
    Button generateAccountBtn;
    Button getAccountsBtn;
    Button paymentSheetBtn;
    Button sentSmartContractBtn;
    private SBlockchain sBlockchain;
    private HardwareWallet wallet;
    private Account generateAccount;


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
        sentSmartContractBtn= findViewById(R.id.sendSmartContract);

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
}
