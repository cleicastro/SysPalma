package com.ryatec.syspalma;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApontamentoFragment extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String os;
    private Integer idPlanejamento;
    private String atividade;
    private String data;
    private String ficha;
    private String nome;
    private String matricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(GetSetCache.getFazendaGet());
        getSupportActionBar().setSubtitle("OS: "+GetSetCache.getOsPlanejamento()+" \t\t"+GetSetCache.getAtividade() +" \t "+GetSetCache.getDataFicha());

        //GetSetCache.setSetSpinnerParcela(0);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.action_settings:
                return true;
            default:break;
        }
        return true;
    }

    @SuppressLint("ValidFragment")
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static ExpansedListiview adapter;
        private ExpandableListView explV;
        private static ArrayList<String> listCategoria;
        private static Map<String, ArrayList<String>> mapChild;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_maquinario, container, false);

            //listCategoria = new ArrayList<>();
            //mapChild = new HashMap<>();
            //explV = (ExpandableListView) rootView.findViewById(R.id.elvCompra);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //conteudoArray();

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        if(GetSetCache.operacao.equals("Poda") || GetSetCache.operacao.equals("Coroamento") || GetSetCache.getAtividadeTipo().equals("Colheita")) {
            adapter.addFrag(new ApontamentoMDO(), "APONTAMENTO");
            //adapter.addFrag(new ApontamentoMDOCQO(), "APONTAMENTO");
        }else if(GetSetCache.getOperacao().equals("Fitossanidade")){
            adapter.addFrag(new ApontamentoMDOFito(), "APONTAMENTO");
        }else{
            adapter.addFrag(new ApontamentoMDOPatrimonio(), "APONTAMENTO");
        }
        adapter.addFrag(new MaoObra(), "REGISTROS");
        adapter.addFrag(new ApontamentoMaquinario(), "MAQUINÁRIO");
        adapter.addFrag(new ApontamentoListaErro(), "ESTATÍSTICAS");

        viewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


}
