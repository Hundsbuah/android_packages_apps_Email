package com.android.email.activity.setup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.email.R;
import com.android.email.activity.UiUtilities;

public class AccountCredentials extends Activity
        implements AccountSetupCredentialsFragment.Callback, View.OnClickListener {

    private View mDoneButton;
    private View mCancelButton;

    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_PROTOCOL = "protocol";

    private static final String CREDENTIALS_FRAGMENT_TAG = "credentials";

    public static Intent getAccountCredentialsIntent(final Context context, final String email,
            final String protocol) {
        final Intent i = new Intent(context, AccountCredentials.class);
        i.putExtra(EXTRA_EMAIL, email);
        i.putExtra(EXTRA_PROTOCOL, protocol);
        return i;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_credentials);
        final String emailAddress = getIntent().getStringExtra(EXTRA_EMAIL);
        final String protocol = getIntent().getStringExtra(EXTRA_PROTOCOL);

        final AccountSetupCredentialsFragment f =
                AccountSetupCredentialsFragment.newInstance(emailAddress, protocol, false);
        getFragmentManager().beginTransaction()
                .add(R.id.account_credentials_fragment_container, f, CREDENTIALS_FRAGMENT_TAG)
                .commit();

        mDoneButton = UiUtilities.getView(this, R.id.done);
        mCancelButton = UiUtilities.getView(this, R.id.cancel);
        mDoneButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        // Assume canceled until we find out otherwise.
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onCredentialsComplete(Bundle results) {
        final Intent intent = new Intent();
        intent.putExtras(results);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == mDoneButton) {
            final AccountSetupCredentialsFragment fragment = (AccountSetupCredentialsFragment)
                    getFragmentManager().findFragmentByTag(CREDENTIALS_FRAGMENT_TAG);
            final Bundle results = fragment.getCredentialResults();
            onCredentialsComplete(results);
        } else if (view == mCancelButton) {
            finish();
        }
    }

    @Override
    public void setNextButtonEnabled(boolean enabled) {
        mDoneButton.setEnabled(enabled);
    }
}