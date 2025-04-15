package com.example.streamingappapi.trailers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamingappapi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TrailersFragment extends Fragment {
    private RecyclerView recVTrailers;
    private TrailerRecItemAdapter trailerRecItemAdapter;
    private List<TrailerItems> trailerItemsList;
    private ArrayList<String> trailers;
    private String title, runtime;

    public static TrailersFragment newInstance(ArrayList<String> trailers, String title, String runtime) {
        TrailersFragment fragment = new TrailersFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("trailers", trailers);
        args.putString("title", title);
        args.putString("runtime", runtime);
        fragment.setArguments(args);
        return fragment;
    }


    private List<TrailerItems> generateTrailerList() {
        List<TrailerItems> itemList = new ArrayList<>();
        itemList.add(new TrailerItems("GOT", Arrays.asList("1 min 26 sec", "1 min 38 sec", "1 min 17 sec", "1 min 43 sec", "1 min 45 sec", "1 min 41 sec", "1 min 48 sec", "2 min 02 sec"), Arrays.asList("https://www.youtube.com/watch?v=bjqEWgDVPe0", "https://www.youtube.com/watch?v=AlhHGUfCYw4", "https://www.youtube.com/watch?v=nblUgAMoOvU", "https://www.youtube.com/watch?v=xZY43QSx3Fk", "https://www.youtube.com/watch?v=F9Bo89m2f6g", "https://www.youtube.com/watch?v=yu8eRaq1FUM", "https://www.youtube.com/watch?v=giYeaKsXnsI", "https://www.youtube.com/watch?v=rlR4PJn8b8I")));
        itemList.add(new TrailerItems("Dark", Arrays.asList("2 min 25 sec", "1 min 44 sec", "2 min 26 sec"),Arrays.asList("https://www.youtube.com/watch?v=rrwycJ08PSA", "https://www.youtube.com/watch?v=810CHvSdXOo", "https://www.youtube.com/watch?v=cq2iTHoLrt0")));
        itemList.add(new TrailerItems("The Boys", Arrays.asList("2 min 15 sec", "3 min 06 sec", "2 min 35 sec", "2 min 48 sec"), Arrays.asList("https://www.youtube.com/watch?v=5SKP1_F7ReE", "https://www.youtube.com/watch?v=MN8fFM1ZdWo", "https://www.youtube.com/watch?v=K-8VYKUZYiw", "https://www.youtube.com/watch?v=EzFXDvC-EwM")));
        itemList.add(new TrailerItems("The 100", Arrays.asList("4 min 18 sec", "1 min 52 sec", "2 min 40 sec", "3 min 09 sec", "1 min 00 sec", "2 min 32 sec", "2 min 10 sec"), Arrays.asList("https://www.youtube.com/watch?v=ia1Fbg96vL0", "https://www.youtube.com/watch?v=IW7_ZzFZF-o", "https://www.youtube.com/watch?v=uwxwHTu802M", "https://www.youtube.com/watch?v=JQi2MAfpACI", "https://www.youtube.com/watch?v=17txKtoXB48", "https://www.youtube.com/watch?v=yxafN3ZWIxY", "https://www.youtube.com/watch?v=9-uMLQq1pdM")));
        itemList.add(new TrailerItems("Breaking Bad", Arrays.asList("2 min 07 sec", "2 min 31 sec", "2 min 07 sec", "2 min 07 sec", "1 min 19 sec"), Arrays.asList("https://www.youtube.com/watch?v=HhesaQXLuRY", "https://www.youtube.com/watch?v=I-8914DuyhY", "https://www.youtube.com/watch?v=ZK2IQ3LbLYk", "https://www.youtube.com/watch?v=mE18hd3NxAs", "https://www.youtube.com/watch?v=_Z-PTJhf0Po")));
        itemList.add(new TrailerItems("Prison Bread", Arrays.asList("1 min 30 sec", "57 sec", "45 sec", "1 min", "1 min 40 sec"), Arrays.asList("https://www.youtube.com/watch?v=AL9zLctDJaU", "https://www.youtube.com/watch?v=sDjfm5ioWMg", "https://www.youtube.com/watch?v=nvEwURsCMG4", "https://www.youtube.com/watch?v=1D62H2UFNqg", "https://www.youtube.com/watch?v=iABCLLOrzZA")));

        return itemList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trailers = getArguments().getStringArrayList("trailers");
            title = getArguments().getString("title");
            runtime = getArguments().getString("runtime");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);

        recVTrailers = view.findViewById(R.id.recVTrailers);

        recVTrailers.setLayoutManager(new LinearLayoutManager(getContext()));
        // Check if trailers list is null or empty
        if (trailers == null || trailers.isEmpty()) {
            // Use the generated list of trailers
            trailerItemsList = generateTrailerList();
            trailerRecItemAdapter = new TrailerRecItemAdapter(getContext(), trailerItemsList, true);
        } else {
            // Use the trailers passed in the arguments
            trailerRecItemAdapter = new TrailerRecItemAdapter(getContext(), trailers, title, runtime);
        }
        recVTrailers.setAdapter(trailerRecItemAdapter);
        recVTrailers.setHasFixedSize(true);

        return view;
    }
}