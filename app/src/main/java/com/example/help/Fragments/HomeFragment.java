package com.example.help.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.help.NewNoteActivity;
import com.example.help.R;
import com.example.help.RecycleViewAdapter;
import com.example.help.UsersContent;
import com.example.help.Utils.EncryptionClass;
import com.example.help.databinding.FragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAG";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String unique_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public final CollectionReference reference = db.collection(unique_user_id);
    public FirebaseAuth.AuthStateListener mAuthListener;
    LinearLayoutManager layoutManager;
    private FragmentHomeBinding binding;
    private RecycleViewAdapter adapter;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);


        if (mToolbar != null) {

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        binding.buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewNoteActivity.class));
            }
        });
        Log.d(TAG, "onCreate: " + unique_user_id);


        layoutManager
                = new LinearLayoutManager(getContext());


        setup_recycler();


        return view;

    }


    public void setup_recycler() {
        Query query = reference.orderBy("site_name");

        FirestoreRecyclerOptions<UsersContent> options = new FirestoreRecyclerOptions.Builder<UsersContent>()
                .setQuery(query, UsersContent.class)
                .build();

        adapter = new RecycleViewAdapter(options, getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);


        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);

                    return;
                }

                assert value != null;
                if (value.getDocuments().size() > 0) { // List is populated

                    Log.d(TAG, "Adapter is not Empty");
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.nothingHere.setVisibility(View.GONE);
                    binding.nothingHereText.setVisibility(View.GONE);

                } else { // List is empty
                    Log.d(TAG, "Adapter is  Empty");

                    binding.recyclerView.setVisibility(View.INVISIBLE);
                    binding.nothingHere.setVisibility(View.VISIBLE);
                    binding.nothingHereText.setVisibility(View.VISIBLE);

                }
            }
        });


        final String[] decryuser_name = new String[1];
        final String[] decryuserpass = new String[1];
        final String[] decrypted_description = new String[1];


        adapter.setOnItemLongClickListener(new RecycleViewAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(int position, DocumentSnapshot documentSnapshot) throws Exception {
                UsersContent usersContent = documentSnapshot.toObject(UsersContent.class);
                usersContent.setDocument_id(documentSnapshot.getId());
                String id = usersContent.getDocument_id();
                String user_name = usersContent.getSite_name();
                String pass = usersContent.getPass();
                String description = usersContent.getDescription();

                decryuser_name[0] = EncryptionClass.decrypt(user_name);
                decryuserpass[0] = EncryptionClass.decrypt(pass);
                decrypted_description[0] = EncryptionClass.decrypt(description);


                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("site_name", decryuser_name[0]);
                intent.putExtra("pass", decryuserpass[0]);
                intent.putExtra("description", decrypted_description[0]);
                startActivity(intent);

                Log.d(TAG, "Document id : -  " + usersContent.getDocument_id());
            }
        });


        adapter.setOnItemClickListener(new RecycleViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, DocumentSnapshot documentSnapshot, View v) throws Exception {
                String iid = UsersContent.getDocument_id();


                UsersContent usersContent = documentSnapshot.toObject(UsersContent.class);
                usersContent.setDocument_id(documentSnapshot.getId());
                String id = usersContent.getDocument_id();
                String user_name = usersContent.getSite_name();
                String pass = usersContent.getPass();
                String description = usersContent.getDescription();


                decryuser_name[0] = EncryptionClass.decrypt(user_name);
                decryuserpass[0] = EncryptionClass.decrypt(pass);
                decrypted_description[0] = EncryptionClass.decrypt(description);
                Log.d(TAG, "onItemClick: " + "\n \n" + decryuser_name);


                View bottom = LayoutInflater.from(getContext()).inflate(
                        R.layout.bottomview,
                        (NestedScrollView) getView().findViewById(R.id.parent));
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
                bottomSheetDialog.setContentView(bottom);
                bottomSheetDialog.show();

                String userbane = Arrays.toString(decryuser_name).replace("[", "").replace("]", "");
                String passrad = Arrays.toString(decryuserpass).replace("[", "").replace("]", "");


                TextView textView = bottomSheetDialog.findViewById(R.id.user);
                textView.setText(userbane);

                TextView ewwrw = bottomSheetDialog.findViewById(R.id.pass);
                ewwrw.setText(passrad);

                Button copy_username = bottomSheetDialog.findViewById(R.id.copy_username);
                Button copy_passoward = bottomSheetDialog.findViewById(R.id.copy_pass);

                copy_username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("User Name", userbane);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Username Copied ", Toast.LENGTH_SHORT).show();

                    }
                });


                copy_passoward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clip = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData da = ClipData.newPlainText("Passoward", passrad);
                        clip.setPrimaryClip(da);


                        Toast.makeText(getActivity(), "Passoward Copied ", Toast.LENGTH_SHORT).show();
                    }
                });


            }


        });


    }




    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}