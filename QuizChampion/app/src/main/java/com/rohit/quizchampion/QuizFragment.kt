package com.rohit.quizchampion

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.rohit.quizchampion.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    data class Question(val question:String,val answers:List<String>)

    private val questions: MutableList<Question> = mutableListOf(
        Question(question = "The first Indian University was opened in 1857 was in",
            answers = listOf("Kolkata","Mumbai","Bihar","Chennai")),
        Question(question = "The largest dry rock in India is situated at",
            answers = listOf("Marmugao","Kolkata","Mumbai","Cochin")),
        Question(question = "The largest Indian State by area is",
            answers = listOf("Rajasthan","Maharashtra","Uttar Pradesh","Madhya Pradesh")),
        Question(question = "Which is the oldest monuments?",
            answers = listOf("Ajanta Caves","Qutub Minar","Khajurah","Taj Mahal")),
        Question(question = "India's first 'Ladies Special' Suburban train was started by which of the Indian Railway Zone?",
            answers = listOf("Western","Southern","Northern","Eastern")),
        Question(question = "The first electric train of India 'Deccan Queen' was run between",
            answers = listOf("Kalyan and Pune","New Delhi and Madras","Bombay and Surat","Howrah and Delhi")),
        Question(question = "Who was the first Indian to go into space?",
            answers = listOf("Rakesh Sharma","Kalpana Chawla","Satish Dawan","Ravi Malhotra")),
        Question(question = "'Apsara' is the name of India's first",
            answers = listOf("Nuclear Reactor","Helicopter","Railway Locomotive","Ground Battle Tank")),
        Question(question = "Which city is called 'White City' of Rajasthan?",
            answers = listOf("Udaipur","Bihar","Jaipur","Jodhpur")),
        Question(question = "Badrinath is situated on the bank of river",
            answers = listOf("Alaknanda","Ganga","Yamuna","Saraswathi"))
    )

    private lateinit var binding:FragmentQuizBinding

    private val TIME_OVER = 0L
    private val ONE_SECOND = 1000L
    private val COUNTDOWN_TIME = 11000L
    //var currentTime : Long = 0
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    private lateinit var timer: CountDownTimer

    lateinit var currentQuestion: Question //Set by setQuestion()
    lateinit var answers: MutableList<String> //Set by setQuestion()
    private var questionIndex = 0
    private var wrongAnswer = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 5)
    var correctAnswers:Int = 0

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Adding timer
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
                binding.timeRemaining.text = "Time Left : "+DateUtils.formatElapsedTime(currentTime.value!!.toLong())
            }

            override fun onFinish() {
                Log.i("QuizFragment","Inside onFinish() for $questionIndex")
                _currentTime.value = TIME_OVER
                questionIndex++
                if(questionIndex<numQuestions)
                {
                    binding.invalidateAll()
                    setQuestion()
                }
                else
                {
                    findNavController().navigate(QuizFragmentDirections.actionQuizFragmentToGameOverFragment(correctAnswers,wrongAnswer))
                }
            }
        }

        Log.i("QuizFragment","onCreate fragment")

        binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater,
            R.layout.fragment_quiz,container,false)
        //Set first question and then randomize the list
        randomizeQuestions()

        binding.game = this

        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.optionsRadioGroup.checkedRadioButtonId

            Log.i("QuizFragment","numberOfQuestions = $numQuestions")
            Log.i("QuizFragment","questionIndex = $questionIndex")

            if (-1 != checkedId) {
                //Storing response in answerIndex
                timer.cancel()
                var answerIndex = 0
                when (checkedId) {
                    R.id.option_two -> answerIndex = 1
                    R.id.option_three -> answerIndex = 2
                    R.id.option_four -> answerIndex = 3
                }

                if(answers[answerIndex] == currentQuestion.answers[0])
                    correctAnswers++
                else
                    wrongAnswer++

                questionIndex++

                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                    binding.invalidateAll()
                }else{
                    view.findNavController().navigate(QuizFragmentDirections.actionQuizFragmentToGameOverFragment(correctAnswers,wrongAnswer))
                }
            }//Submit button action ends
        }
        return binding.root
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        Log.i("QuizFragment","setQuestion() called")
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}