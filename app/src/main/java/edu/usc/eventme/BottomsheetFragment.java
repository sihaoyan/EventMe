package edu.usc.eventme;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import  edu.usc.eventme.databinding.BottomsheetLayoutBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class BottomsheetFragment extends BottomSheetDialogFragment {


    BottomSheetBehavior bottomSheetBehavior;
    BottomsheetLayoutBinding bi;
    private RecyclerView ry;
    private Button back;
    private double currentlat=0;
    private double currentlon=0;

    private Event currentevent=new Event();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.bottomsheet_layout, null);
        //setting layout with bottom sheet
        bottomSheet.setContentView(view);
        //binding views to data binding.
        bi = DataBindingUtil.bind(view);


        String currentid=getArguments().getString("currentid");
        currentlat=getArguments().getDouble("lat");
        currentlon=getArguments().getDouble("lon");
        //System.out.println("id!!!!!!!"+currentid);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EventList results = new EventList();
        db.collection("events").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("succeed!!!!!\n");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                if(event.getId().equals(currentid)){
                                    currentevent=event;
                                    bi.currentevent.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            System.out.println("click!!!!!!!!!!");
                                            Intent intent = new Intent(v.getContext(), EventRegisterActivity.class);
                                            intent.putExtra("Event",currentevent);
                                            v.getContext().startActivity(intent);
                                        }
                                    });
                                    Picasso.get().load(currentevent.getPhotoURL()).into(bi.currenteventImage);
                                    System.out.println("title!!!!!!!"+currentevent.getEventTitle());
                                    bi.currenteventName.setText(currentevent.getEventTitle());
                                    //System.out.println(list.get(position).getEventTitle()+"!!!!!!!!!!!!");
                                    bi.currentdistance.setText(String.format("%.2f", currentevent.findDis(currentlat, currentlon))+"miles");
                                    bi.currenteventLocation.setText(currentevent.getLocation());
                                    bi.currenteventDate.setText(currentevent.getStartDate()+" to "+currentevent.getEndDate());
                                    bi.currenteventTime.setText(currentevent.getStartTime()+" to "+currentevent.getEndTime());
                                    bi.currenteventCost.setText(currentevent.getCost());
                                    bi.currentsponceringOrganization.setText(currentevent.getSponsoringOrganization());
                                }
                                else {
                                    results.addEvent(event);
                                }
                                //System.out.println(event.getLocation());
                            }
                            results.sortbydistoloc(currentlat,currentlon);
                        }
                        else {
                            System.out.println("No Event"+ task.getException().getMessage());
                        }
                    }
                });
        ry = bi.recyclerView;

        back = bi.back;
        Intent toMain = new Intent(getActivity(),MainActivity.class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        MyAdaptor myAdaptor = new MyAdaptor(getActivity(), results);
        ry.setAdapter(myAdaptor);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ry.setLayoutManager(llm);
        ry.smoothScrollToPosition(0);
        ry.setHasFixedSize(false);
        ry.setNestedScrollingEnabled(false);
        //Picasso.get().load(currentevent.getPhotoURL()).into(bi.currenteventImage);



        //tv.setOnClickListener(this::onClickTV);





        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());


        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight((Resources.getSystem().getDisplayMetrics().heightPixels)/2);
        bottomSheetBehavior.setMaxHeight((Resources.getSystem().getDisplayMetrics().heightPixels));

        //setting max height of bottom sheet
        //bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));
        //addEventlsit();
        //addEventlsit();
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_EXPANDED == i) {
                    showView(ry, (int)((Resources.getSystem().getDisplayMetrics().heightPixels)/2.5* myAdaptor.getItemCount()));
                    //addEventlsit();
                    //hideAppBar(bi.profileLayout);

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == i) {
                    //showView(bi.bottomSheet, (Resources.getSystem().getDisplayMetrics().heightPixels));
                    //ry.smoothScrollBy(10,10);
                    showView(bi.recyclerView, 0);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        //aap bar cancel button clicked
//        bi.cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dismiss();
//            }
//        });


        //hiding app bar at the start
        //hideAppBar(bi.appBarLayout);


        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();

        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

//    private void hideAppBar(View view) {
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        params.height = 0;
//        view.setLayoutParams(params);
//
//    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

//    private void addEventlsit(){
//        LinearLayout nv= (LinearLayout) bi.eventlist;
//
//
//        View ebview =  View.inflate(getContext(), R.layout.eventbox_layout, null);
//
//        CardView tv=ebview.findViewById(R.id.cardView);
//
//        //tv.setTextColor(Color.GREEN);
//        //tv.setBackgroundColor(Color.GREEN);
////        if(tv.getParent() != null) {
////            ((ViewGroup)tv.getParent()).removeView(tv); // <- fix
////        }
//        nv.addView(tv);
//    }


//    private int getActionBarSize() {
//        final TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
//        int size = (int) array.getDimension(0, 0);
//        return size;
//    }
//    public void gotodetail(View view){
//        TextView tv = (TextView) view;
//        System.out.println("click!!!!!!!!!!");
//        Intent intent = new Intent(view.getContext(), Details.class);
//        //intent.putExtra("Events",result);
//        //intent.putExtra("position",getAdapterPosition());
//        view.getContext().startActivity(intent);
//    }
}