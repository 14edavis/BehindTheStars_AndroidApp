package org.storiesbehindthestars.wwiifallenapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.storiesbehindthestars.wwiifallenapp.components.MemorialView;
import org.storiesbehindthestars.wwiifallenapp.models.Story;
import org.storiesbehindthestars.wwiifallenapp.presenters.StoryPresenter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class StoryActivity extends AppCompatActivity implements StoryPresenter.MVPView {
    MemorialView memorialView;
    StoryPresenter presenter;
    Story story;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new StoryPresenter(this);
        story = (Story) getIntent().getSerializableExtra(StoriesActivity.STORY);

        // THE BACK BUTTON at the top. Also had to list the parent activity in the manifest
        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true); //TODO: This goes back to an empty activity... figure out why
        ab.setTitle(story.name);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(this);
        memorialView = new MemorialView(this, story, true);

//        //hardcoded for testing
//        String name = "Thomas T Takao";
//        String story = "Thomas Tamotsu Takao, a Japanese American, was born on March 28th, 1921 in Stockton, San Joaquin, California. His father, Monkichi, immigrated to America in 1904 and worked as a gardener doing odd jobs. His mother, Akino, immigrated in 1912 (possibly from Kyushu, Japan) and worked as a cook before having children. She still couldn’t speak English by the time Thomas was reaching his teenage years. Thomas had a little brother named Masuo, who was 2 years younger than him, and a little sister named Mary who was 8 years younger. \n\nBy 1940, Thomas lived in San Francisco working as a driver for someone named Dr. Keller. Thomas had brown eyes and black hair, stood 5’8” tall, and weighed 142 lb.\n\nAccording to [100thbattalion.org](https://www.100thbattalion.org/), after Roosevelt’s Executive Order 9066, Takao was part of the first group that “voluntarily evacuate” their homes on April 6, 1942, and were imprisoned in behind barbed wire at the Santa Anita Race Track Assembly Center for six months. “On October 28, 1942, [Thomas] was shipped inland to the Topaz Central Utah project.” \n\nThe Topaz Internment Camp was set up for agricultural work, so most of the internees spent their time raising crops and animals. As a single man, Thomas likely bunked in the barracks with four other strangers. A barbed wire fence and manned watchtowers with searchlights surrounded the farmlands, schools, and hospital, and churches making up the internment camp.\n\nThe next year, the U.S. War Relocation Authority (WRA) created a questionnaire to determine the “Americanness” of the imprisoned Japanese. The questionnaire started by asking about age, sex, and citizenship status. It continued on to ask about, religion skill levels with Japanese and English, preferred sports and hobbies, clubs, and newspapers. The WRA scored each answer, determining that the internees were “more American” if they gave answers like baseball and Christian, and “more Japanese” if they answered with kendo or Buddhism. \n\nThe final answers asked: _Are you willing to serve in the armed forces of the United States on combat duty, wherever ordered? _and _Will you swear unqualified allegiance to the United States of America and faithfully defend the United States ... and forswear any form of allegiance or obedience to the Japanese emperor?_ Thomas answered “yes” to both of the final questions. He was inducted into the U.S. armed forces at Fort Douglas, Utah on May 5th, 1943.\n\nThomas trained with the 442nd Regimental Combat Team (RCT), which eventually joined the 100th battalion in Italy. He served with bravery and dedication has he rose from Private to Sergeant. He fought in Rome to Arno, the Battle of Bruyeres and the Lost Battalion, the Champagne Campaign, and the Po Valley Campaign. Thomas received the Bronze Star; 2 Silver Stars, one with an Oak Leaf Cluster; the Purple Heart; Distinguished Unit Citations with Oak Leaf Cluster; Combat Infantryman Badge; African Middle East European Campaign Medal; and the World War II Victory Medal ([100thbattalion.org](http://www.100thbattalion.org/)). His award citations tell of some of Thomas’s acts of bravery.\n\nOn July 12th, 1944 near Pastina, Italy, Thomas’s courage and initiative gained valuable information that led to his battalion's successful attack on the town of Pastina. “As a member of a reconnaissance patrol, Private First Class Takao advanced to within ten feet of the enemy outposts in order to gain information. Disregarding the platoon leader's instructions to withdraw when almost surrounded by the enemy, Private First Class Takao continued observing the enemy movements. Finally, in withdrawing, he was forced to fight his way back against a numerically superior force. The courage and initiative displayed by Private First Class Takao and the valuable information he had gained were of inestimable value to his battalion's successful attack on the town of Pastina.” (1st Silver Star Medal Award Citation)\n\nThomas’s battalion then relocated to France for a time under General Dwight Eisenhower. On October 28th, 1944 near Biffontaine, France, Thomas displayed courage, determination, and disregard for personal safety to help neutralize an enemy strong-point. “While a member of a four man reconnaissance patrol charged with the mission of obtaining information on the disposition of the enemy, Sergeant Takao moved under the cover of darkness around the enemy's left flank and to within ten yards of his position. Opening fire at point blank range, the patrol caught the defenders completely by surprise, killing three of the enemy and forcing ten others to surrender.” (2nd Silver Star Medal Award Citation)\n\nThe next year, Thomas and his battalion returned to Italy to help break through the Gothic Line, which was defended by over “2,000 machine gun nests, casemates, bunkers, observation posts, and artillery fighting positions” ([https://en.wikipedia.org/wiki/Gothic\\_Line](https://en.wikipedia.org/wiki/Gothic_Line)). “During the first few hundred yards of advance, eight mines were blown, [and] heavy machinegun fire was encountered,” fellow solider Sergeant Chester Tanaka later wrote. “In spite of the murderous fire, the 100th took the heat, drew the enemy’s attention, and accomplished its diversionary mission. The 2nd and 3rd battalions’ surprise attack while the Germans were occupied with the 100th was a complete success.”\n\nThomas was killed in action on April 5th, 1945 while cracking the Gothic Line. His remains were eventually buried at his parent’s plot in Japan.\n\nThank you for your sacrifice, especially for a country who did not treat you as they should have.\n\n------------\n\nThis story is part of the Stories Behind the Stars project (see[ www.storiesbehindthestars.org](http://www.storiesbehindthestars.org/)). This is a national effort of volunteers to write the stories of all 400,000+ of the US WWII fallen here on Fold3. **Can you help write these stories**? Related to this, there will be a smart phone app that will allow people to visit any war memorial or cemetery, scan the fallen's name and read his/her story.";
//        memorialView.setText(name, story);

        scrollView.addView(memorialView);
        mainLayout.addView(scrollView);
        setContentView(mainLayout);

        memorialView.setOnClickListener((view)->{
            presenter.handleViewButtonClick();
        });
    }

    public void goToFold3Page(){
        Uri webpage = Uri.parse(story.storyURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


//    public void openWebPage(String url) {
//        Uri webpage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
}