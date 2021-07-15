package com.example.help;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.help.Utils.EncryptionClass;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import es.dmoral.toasty.Toasty;
import id.ionbit.ionalert.IonAlert;

public class RecycleViewAdapter extends FirestoreRecyclerAdapter<UsersContent, RecycleViewAdapter.dataHolder> {




    public onLongItemClickListener mlistener;
    public onItemClickListener mmlistener;

    private RecyclerView.Adapter i;
    private Context context;

    public RecycleViewAdapter(@NonNull FirestoreRecyclerOptions<UsersContent> options , Context context) {
        super(options);
        this.context=context;


    }

    @Override
    protected void onBindViewHolder(@NonNull dataHolder holder, int position, @NonNull UsersContent model) {

        UsersContent m = model;
        String encrypted_sitename = m.getSite_name();
        String encrypted_passward = m.getPass();
        String encrypted_description = m.getDescription();


        String decrypt_sitename = "";
        String decrypt_passward = "";
        String decrypted_description = "";
        try {
            decrypt_sitename = EncryptionClass.decrypt(encrypted_sitename);
            decrypt_passward = EncryptionClass.decrypt(encrypted_passward);
            decrypted_description = EncryptionClass.decrypt(encrypted_description);
            Log.d("TEST", "decrypt_sitename:" + decrypt_sitename);
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.site_name.setText(decrypt_sitename);
        holder.pass.setText(decrypt_passward);
        Log.d("TAG", "onBindViewHolder: " + model.getSite_name() + " " + model.getPass());
    }



    @NonNull
    @Override
    public dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_lay, parent, false);
        return new dataHolder(v);
    }

    public void delete(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    public interface onLongItemClickListener {
        void onLongItemClick(int position, DocumentSnapshot documentSnapshot) throws Exception;
    }


    public interface onItemClickListener {
        void onItemClick(int position, DocumentSnapshot documentSnapshot, View v) throws Exception;
    }



    public void setOnItemLongClickListener(onLongItemClickListener listener) {
        this.mlistener = listener;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.mmlistener = listener;
    }



    class dataHolder extends RecyclerView.ViewHolder {
        public TextView site_name;
        public TextView pass;
        public ImageButton btn;


        public dataHolder(@NonNull View itemView) {
            super(itemView);
            site_name = (TextView) itemView.findViewById(R.id.text_view_site_name);
            pass = (TextView) itemView.findViewById(R.id.text_view_pass);
            btn = (ImageButton) itemView.findViewById(R.id.delete_btn);





            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final IonAlert sure_delete = new IonAlert(context, IonAlert.ERROR_TYPE);


                    sure_delete.setTitleText("Are you sure?")
                            .setContentText("You Won't be able to recover this file!")
                            .setCancelText("No,cancel !")
                            .setConfirmText("Yes,delete it!")
                            .showCancelButton(false)
                            .setConfirmClickListener(new IonAlert.ClickListener() {
                                @Override
                                public void onClick(IonAlert ionAlert) {

                                    int pos = getAdapterPosition();
                                    if (pos != RecyclerView.NO_POSITION) {
                                        try {
                                            delete(getAdapterPosition());
                                            Toasty.error(context,"Deleted Successfully",Toast.LENGTH_SHORT,true).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    sure_delete.dismiss();
                                }
                            })
                            .setCancelClickListener(new IonAlert.ClickListener() {
                                @Override
                                public void onClick(IonAlert sDialog) {
                                    sure_delete.dismiss();
                                }
                            })
                            .show();







                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mlistener != null) {
                        try {
                            mlistener.onLongItemClick(getAdapterPosition(), getSnapshots().getSnapshot(pos));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mlistener != null) {
                        try {
                            mmlistener.onItemClick(getAdapterPosition(), getSnapshots().getSnapshot(pos), view);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
    }


}
